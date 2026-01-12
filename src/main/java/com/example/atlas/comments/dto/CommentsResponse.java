package com.example.atlas.comments.dto;

import com.example.atlas.comments.Comments;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record CommentsResponse(
        Long id,
        String content,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime createdAt,
        String username,
        String taskName
) {

    public CommentsResponse(Comments comments) {
        this(
                comments.getId(),
                comments.getContent(),
                comments.getCreatedAt(),
                comments.getUsers().getName(),
                comments.getTask().getTitle()
        );
    }
}
