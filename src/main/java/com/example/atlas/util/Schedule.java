package com.example.atlas.util;

import com.example.atlas.resetPassword.ResetPasswordRepo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class Schedule {

    private final ResetPasswordRepo resetPasswordRepo;

    public Schedule(ResetPasswordRepo resetPasswordRepo) {
        this.resetPasswordRepo = resetPasswordRepo;
    }

    @Scheduled(fixedDelay = 1000*60)
    @Async
    @Transactional
    public void deleteResetPassword() {
        resetPasswordRepo.deleteByUsed(true);
        resetPasswordRepo.deleteByExpireAt(LocalDateTime.now());
    }
}
