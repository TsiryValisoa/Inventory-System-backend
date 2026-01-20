package mg.tsiry.invetory_management_system.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email is required!")
    @Email(message = "Email format must be valid")
    private String email;

    @NotBlank(message = "Password is required!")
    private String password;

}
