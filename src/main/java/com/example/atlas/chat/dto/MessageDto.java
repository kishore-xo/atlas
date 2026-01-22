package com.example.atlas.chat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MessageDto( String username, @NotNull String content) {
}
