package mg.tsiry.invetory_management_system.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetRequest {

    @NotBlank(message = "Email is required!")
    private String email;
}
