package com.example.atlas.util;

import com.example.atlas.refreshToken.RefreshTokenRepo;
import com.example.atlas.resetPassword.ResetPasswordRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class Schedule {

    private final ResetPasswordRepo resetPasswordRepo;
    private final RefreshTokenRepo refreshTokenRepo;

    public Schedule(ResetPasswordRepo resetPasswordRepo, RefreshTokenRepo refreshTokenRepo) {
        this.resetPasswordRepo = resetPasswordRepo;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    @Scheduled(fixedDelay = 1000 * 60)
    @Transactional
    public void deleteResetPassword() {
        resetPasswordRepo.deleteByUsed(true);
        resetPasswordRepo.deleteByExpireAt(LocalDateTime.now());
    }

    @Scheduled(fixedDelay = 1000 * 60)
    @Transactional
    public void deleteRefreshToken() {
        refreshTokenRepo.deleteRefreshTokenByExpireTime(Instant.now());
    }
}
