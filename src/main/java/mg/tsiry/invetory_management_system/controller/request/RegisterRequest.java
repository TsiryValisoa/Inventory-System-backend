package mg.tsiry.invetory_management_system.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mg.tsiry.invetory_management_system.enums.UserRole;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email format must be valid")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;

    private UserRole role;

}
