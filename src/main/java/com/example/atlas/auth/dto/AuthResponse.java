package com.example.atlas.auth.dto;

public record AuthResponse(String token, Long userId, String username, String email) {
}
