package mg.tsiry.invetory_management_system.data.repositories;

import mg.tsiry.invetory_management_system.data.entities.RefreshToken;
import mg.tsiry.invetory_management_system.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
}
