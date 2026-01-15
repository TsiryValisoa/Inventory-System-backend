package mg.tsiry.invetory_management_system.service.impl;

import lombok.RequiredArgsConstructor;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.data.entities.ResetToken;
import mg.tsiry.invetory_management_system.data.entities.User;
import mg.tsiry.invetory_management_system.data.repositories.ResetTokenRepository;
import mg.tsiry.invetory_management_system.data.repositories.UserRepository;
import mg.tsiry.invetory_management_system.dto.UserDto;
import mg.tsiry.invetory_management_system.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final ResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder().withoutPadding();

    @Value("${frontendUrl}")
    private String frontendUrl;

    @Override
    public GlobalResponse processResetRequest(String email) {

        userRepository.findByEmail(email).ifPresent(user -> {

            String token = generateResetToken();

            ResetToken resetToken = new ResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiredAt(LocalDateTime.now().plusMinutes(20));

            resetTokenRepository.save(resetToken);
            sendEmail(user.getEmail(), token);
        });

        return GlobalResponse.builder()
                .status(200)
                .message("If the email is registered, you'll get a reset link.")
                .build();
    }

    @Override
    public GlobalResponse validateResetToken(String token) {

         Optional<ResetToken> resetToken = resetTokenRepository.findByToken(token);

         if (resetToken.isPresent() || resetToken.get().isExpired()) {
             return GlobalResponse.builder()
                     .status(404)
                     .message("Invalid or expired token.")
                     .build();
         }

         return GlobalResponse.builder()
                 .status(200)
                 .message("Token is valid")
                 .build();
    }

    @Override
    public GlobalResponse resetPassword(UserDto userDto) {

        Optional<ResetToken> userResetToken = resetTokenRepository.findByToken(userDto.getToken());

        if (userResetToken.isEmpty() || userResetToken.get().isExpired()) {
            return GlobalResponse.builder()
                    .status(404)
                    .message("Invalid or expired token.")
                    .build();
        }

        ResetToken resetToken = userResetToken.get();
        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);

        resetTokenRepository.delete(resetToken);

        return GlobalResponse.builder()
                .status(200)
                .message("Password successfully reset.")
                .build();
    }

    private String generateResetToken() {

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        return base64Encoder.encodeToString(randomBytes);
    }

    private void sendEmail(String email, String token) {

        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        String body = "Click teh link below to reset your password:\n" + resetUrl;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Password reset");
        mailMessage.setText(body);

        javaMailSender.send(mailMessage);
    }
}
