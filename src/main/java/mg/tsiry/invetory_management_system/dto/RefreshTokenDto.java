package mg.tsiry.invetory_management_system.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class RefreshTokenDto {
    private String token;
    private Instant expiryDate;
}
