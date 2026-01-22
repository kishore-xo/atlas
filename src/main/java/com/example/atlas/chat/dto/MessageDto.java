package com.example.atlas.chat.dto;

import jakarta.validation.constraints.NotNull;

public record MessageDto( @NotNull String content) {
}
