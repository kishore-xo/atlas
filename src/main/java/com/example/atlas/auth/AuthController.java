package com.example.atlas.auth;

import com.example.atlas.auth.dto.AuthRequest;
import com.example.atlas.auth.dto.AuthResponse;
import com.example.atlas.resetPassword.ResetPasswordService;
import com.example.atlas.users.dto.UserRequest;
import com.example.atlas.users.dto.UserResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ResetPasswordService resetPasswordService;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody UserRequest userRequest) {
        return authService.register(userRequest);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        return authService.login(authRequest, response);
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@CookieValue("refreshToken") String token) {
        return authService.refreshToken(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue("refreshToken") String token, HttpServletResponse response) {
        authService.logout(token, response);
        return ResponseEntity.ok("Logout");
    }

    @PostMapping("/forgetPassword")
    public void forgetPassword(@RequestParam String email) {
        resetPasswordService.forgetPassword(email);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        return ResponseEntity.ok(resetPasswordService.resetPassword(token, newPassword));
    }
}
