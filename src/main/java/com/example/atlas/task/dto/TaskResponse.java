package com.example.atlas.task.dto;

import com.example.atlas.comments.dto.CommentsResponse;
import com.example.atlas.task.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TaskResponse
        (Long id, String title, String description,
         TaskStatus status, Long workId,
         List<CommentsResponse> commentsResponseList, LocalDateTime createdAt
         )
{}
