package com.example.atlas.comments.dto;

import com.example.atlas.comments.Comments;

import java.time.LocalDateTime;

public record CommentsResponse(Long id, String content, LocalDateTime createdAt, Long userId, Long taskId) {

    public CommentsResponse(Comments comments) {
        this(
                comments.getId(),
                comments.getContent(),
                comments.getCreatedAt(),
                comments.getUsers().getId(),
                comments.getTask().getId()
        );
    }
}
