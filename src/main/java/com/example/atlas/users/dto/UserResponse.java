package com.example.atlas.users.dto;

import com.example.atlas.users.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;


public record UserResponse
        (Long id,
         String name,
         String email,
         Role role,
         @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime createdAt)
{}
