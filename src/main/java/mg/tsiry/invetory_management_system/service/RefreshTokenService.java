package mg.tsiry.invetory_management_system.service;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.RefreshTokenDto;

public interface RefreshTokenService {

    RefreshTokenDto createRefreshToken(Long userId);
    GlobalResponse refreshToken(RefreshTokenDto refreshTokenDto);
    GlobalResponse logoutUser(RefreshTokenDto refreshTokenDto);
}
