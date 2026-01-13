package mg.tsiry.invetory_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.data.entities.RefreshToken;
import mg.tsiry.invetory_management_system.data.entities.User;
import mg.tsiry.invetory_management_system.data.repositories.RefreshTokenRepository;
import mg.tsiry.invetory_management_system.data.repositories.UserRepository;
import mg.tsiry.invetory_management_system.dto.RefreshTokenDto;
import mg.tsiry.invetory_management_system.exception.InvalidCredentialsException;
import mg.tsiry.invetory_management_system.exception.NotFoundException;
import mg.tsiry.invetory_management_system.security.JwtUtils;
import mg.tsiry.invetory_management_system.service.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    @Value("${refreshTokenTime}")
    private long refreshTokenTime;

    @Override
    public RefreshTokenDto createRefreshToken(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        var token = refreshTokenRepository.findByUser(user)
                .orElseGet(() -> {
                    RefreshToken refreshToken = new RefreshToken();
                    refreshToken.setUser(user);
                    return refreshToken;
                });

        token.setExpiryDate(Instant.now().plusMillis(refreshTokenTime));
        token.setToken(UUID.randomUUID().toString());

        var refreshTokenSaved = refreshTokenRepository.save(token);

        return  modelMapper.map(refreshTokenSaved, RefreshTokenDto.class);

    }

    @Override
    public GlobalResponse refreshToken(RefreshTokenDto refreshTokenDto) {

        return refreshTokenRepository.findByToken(refreshTokenDto.getToken())
                .map(token -> {
                    if (isTokenExpired(token)) {
                        refreshTokenRepository.delete(token);
                        return GlobalResponse.builder()
                                .status(400)
                                .message("Refresh token expired. Please login again!")
                                .build();
                    }
                    String newJwt = jwtUtils.generateToken(token.getUser().getEmail());
                    return GlobalResponse.builder()
                            .status(200)
                            .message("new token generated.")
                            .token(newJwt)
                            .expirationTime("15 minutes")
                            .build();
                })
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token."));
    }

    @Override
    public GlobalResponse logoutUser(RefreshTokenDto refreshTokenDto) {

        if (refreshTokenDto.getToken() == null || refreshTokenDto.getToken().isBlank()) {
            return GlobalResponse.builder()
                    .status(400)
                    .message("Refresh token is required!")
                    .build();
        }

        return refreshTokenRepository.findByToken(refreshTokenDto.getToken())
                .map(token -> {
                    refreshTokenRepository.delete(token);
                    return GlobalResponse.builder()
                            .status(200)
                            .message("Logged out successfully.")
                            .build();
                })
                .orElseThrow(() -> new InvalidCredentialsException("Invalid refresh token"));
    }

    private boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

}
