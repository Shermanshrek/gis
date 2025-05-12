package ru.gisback.config;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.gisback.repositories.RefreshTokenRepo;

import java.time.Instant;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final RefreshTokenRepo refreshTokenRepository;

    @Transactional
    @Scheduled(fixedRate = 86400000)
    public void purgeExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens(Instant.now());
    }
}
