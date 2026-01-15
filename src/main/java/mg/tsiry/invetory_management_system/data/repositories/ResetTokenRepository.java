package mg.tsiry.invetory_management_system.data.repositories;

import mg.tsiry.invetory_management_system.data.entities.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

     Optional<ResetToken> findByToken(String token);
     void deleteAllByExpiredAtBefore(LocalDateTime dateTime);
}
