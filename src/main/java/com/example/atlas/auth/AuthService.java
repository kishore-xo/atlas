package com.example.atlas.auth;

import com.example.atlas.auth.dto.AuthRequest;
import com.example.atlas.auth.dto.AuthResponse;
import com.example.atlas.exception.BadRequestException;
import com.example.atlas.refreshToken.RefreshToken;
import com.example.atlas.refreshToken.RefreshTokenService;
import com.example.atlas.util.JwtUtil;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.Users;
import com.example.atlas.users.UserService;
import com.example.atlas.users.dto.UserRequest;
import com.example.atlas.users.dto.UserResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public UserResponse register(UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    public AuthResponse login(AuthRequest request, HttpServletResponse response) {
        Users user = userRepo.findUsersByEmail(request.getEmail()).orElseThrow(() -> new BadRequestException("Invalid credentials"));
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);
        String refreshToken = refreshTokenService.generateRefreshToken(request.getEmail());

        ResponseCookie responseCookie = ResponseCookie.from(
                        "refreshToken", refreshToken
                )
                .maxAge(Duration.ofDays(7))
                .secure(false)
                .path("/auth")
                .httpOnly(true)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail());
    }

    public AuthResponse refreshToken(String token) {
        RefreshToken refreshToken = refreshTokenService.vaildRefreshToken(token);
        String generatedToken = jwtUtil.generateToken(refreshToken.getUsers());
        return new AuthResponse(
                generatedToken,
                refreshToken.getUsers().getId(),
                refreshToken.getUsers().getName(),
                refreshToken.getUsers().getEmail());
    }

    public void logout(String token, HttpServletResponse response) {
        refreshTokenService.deleteRefreshToken(token);

        ResponseCookie cookie = ResponseCookie.from(
                        "refreshToken", ""
                )
                .path("/auth")
                .httpOnly(true)
                .maxAge(0)
                .sameSite("Strict")
                .secure(false)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    }
}
