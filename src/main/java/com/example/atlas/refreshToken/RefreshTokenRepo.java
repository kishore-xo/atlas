package com.example.atlas.refreshToken;

import com.example.atlas.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
    void deleteByUsers(Users users);

    void deleteByToken(String token);

    Optional<RefreshToken> findByToken(String token);
}
