package com.example.atlas.refreshToken;

import com.example.atlas.exception.ForbiddenException;
import com.example.atlas.exception.NotFoundException;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepo tokenRepo;
    private final UserRepo userRepo;


    private static final Duration EXPIRE_TIME = Duration.ofDays(7);


    public String generateRefreshToken(String email) {
        Users users = userRepo.findUsersByEmail(email).orElseThrow(
                () -> new NotFoundException("User not found")
        );
        tokenRepo.deleteByUsers(users);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .users(users)
                .expireTime(Instant.now().plus(EXPIRE_TIME))
                .build();
        return tokenRepo.save(refreshToken).getToken();
    }

    public RefreshToken vaildRefreshToken(String token) {
        RefreshToken refreshToken = tokenRepo.findByToken(token).orElseThrow(
                () -> new ForbiddenException("Invalid refresh token")
        );
        if (refreshToken.getExpireTime().isBefore(Instant.now())) {
            tokenRepo.delete(refreshToken);
            throw new RuntimeException("Token Expired");
        }
        return refreshToken;
    }

    public void deleteRefreshToken(String token) {
        tokenRepo.deleteByToken(token);
    }
}
