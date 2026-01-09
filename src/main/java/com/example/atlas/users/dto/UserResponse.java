package com.example.atlas.users.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;


public record UserResponse
        (Long id,String name,String email,String password,@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime createdAt)
{}
