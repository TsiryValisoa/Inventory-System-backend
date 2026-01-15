package mg.tsiry.invetory_management_system.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reset_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetToken {

    @Id
    @GeneratedValue
    private Long id;

    private String token;
    private LocalDateTime expiredAt;

    @ManyToOne
    private User user;

    public boolean isExpired() {
        return expiredAt.isBefore(LocalDateTime.now());
    }
}
