package mg.tsiry.invetory_management_system.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String token;

    @NotBlank(message = "Password is required!")
    private String password;
}
