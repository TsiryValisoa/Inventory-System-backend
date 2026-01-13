package mg.tsiry.invetory_management_system.controller.request;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @Valid
    private String token;
}
