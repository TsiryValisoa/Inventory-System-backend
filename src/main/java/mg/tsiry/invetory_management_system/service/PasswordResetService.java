package mg.tsiry.invetory_management_system.service;

import mg.tsiry.invetory_management_system.controller.response.GlobalResponse;
import mg.tsiry.invetory_management_system.dto.UserDto;

public interface PasswordResetService {

    GlobalResponse processResetRequest(String email);
    GlobalResponse validateResetToken(String token);
    GlobalResponse resetPassword(UserDto userDto);
}
