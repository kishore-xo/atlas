package com.example.atlas.comments.dto;

import java.time.LocalDateTime;

public record CommentsResponse(Long id, String content, LocalDateTime createdAt, Long userId, Long taskId) {
}
