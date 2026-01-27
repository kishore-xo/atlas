package com.example.atlas.refreshToken;

import com.example.atlas.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    void deleteByUsers(Users users);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.token = :token")
    void deleteByToken(String token);

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE  r.expireTime < :expireTime")
    void deleteRefreshTokenByExpireTime(Instant expireTime);
}
