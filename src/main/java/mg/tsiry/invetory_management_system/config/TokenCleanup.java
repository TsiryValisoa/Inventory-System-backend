package mg.tsiry.invetory_management_system.config;

import lombok.AllArgsConstructor;
import mg.tsiry.invetory_management_system.data.repositories.ResetTokenRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class TokenCleanup {

    private final ResetTokenRepository resetTokenRepository;

    @Scheduled(fixedRate = 3_600_000)
    @Transactional
    public void clearExpiredTokens() {
        resetTokenRepository.deleteAllByExpiredAtBefore(LocalDateTime.now());
    }
}
