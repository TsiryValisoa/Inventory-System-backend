package mg.tsiry.invetory_management_system.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import mg.tsiry.invetory_management_system.controller.request.ResetPasswordRequest;
import mg.tsiry.invetory_management_system.controller.request.ResetRequest;
import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.UserDto;
import mg.tsiry.invetory_management_system.service.PasswordResetService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Tsiry Valisoa
 */
@RestController
@RequestMapping("/api/reset-password")
@AllArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<GlobalResponse> requestResetPassword(@RequestBody @Valid ResetRequest resetRequest) {
        return ResponseEntity.ok(passwordResetService.processResetRequest(resetRequest.getEmail()));
    }

    @GetMapping
    public ResponseEntity<GlobalResponse> validateToken(@RequestParam String token) {
        return ResponseEntity.ok(passwordResetService.validateResetToken(token));
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPassword) {
        UserDto userDto = modelMapper.map(resetPassword, UserDto.class);
        return ResponseEntity.ok(passwordResetService.resetPassword(userDto));
    }
}
