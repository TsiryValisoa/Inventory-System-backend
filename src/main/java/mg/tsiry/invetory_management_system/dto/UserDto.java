package mg.tsiry.invetory_management_system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import mg.tsiry.invetory_management_system.enums.UserRole;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;

    @JsonIgnore
    private String password;

    private String phoneNumber;
    private UserRole role;
    private LocalDateTime createdAt;
    private boolean isActive;
    private String disabledBy;
    private String enabledBy;
}
