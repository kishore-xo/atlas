package com.example.atlas.resetPassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetPasswordRepo extends JpaRepository<ResetPassword, Long> {
    Optional<ResetPassword> findByToken(String token);

    void deleteByExpireAt(LocalDateTime expireAt);

    void deleteByUsed(boolean isUsed);
}
