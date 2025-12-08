package mg.tsiry.invetory_management_system.data.entities;

import jakarta.persistence.*;
import lombok.*;
import mg.tsiry.invetory_management_system.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;
    private String password;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Transaction> transactions;

    private final LocalDateTime createdAt = LocalDateTime.now();
    private boolean isActive = true;
    private String disabledBy;
    private String enabledBy;

}
