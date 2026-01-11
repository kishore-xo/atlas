package com.example.atlas.jwt;

import com.example.atlas.users.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "this_is_a_secret_key_to_generate_json_tokens";
    private final static long EXPIRE_TIME = 1000 * 60 * 60;

    private static Key init() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Users users) {
        return Jwts.builder()
                .subject(users.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(init())
                .claim("role", users.getRole().name())
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) init())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValidateToken(String token) {
        return isTokenExpired(token);

    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

}
