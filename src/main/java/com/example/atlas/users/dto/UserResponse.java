package com.example.atlas.users.dto;

import com.example.atlas.users.Role;
import com.example.atlas.users.Users;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;


public record UserResponse
        (Long id,
         String name,
         String email,
         Role role,
         @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime createdAt) {
    public UserResponse(Users users) {
        this(users.getId(), users.getName(), users.getEmail(), users.getRole(), users.getCreatedAt());
    }
}
