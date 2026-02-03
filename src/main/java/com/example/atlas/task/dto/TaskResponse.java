package com.example.atlas.task.dto;

import com.example.atlas.comments.Comments;
import com.example.atlas.task.Task;
import com.example.atlas.task.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record TaskResponse
        (Long id, String title, String description,
         TaskStatus status, String workSpaceName,
         String fileName,
         @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
         LocalDateTime createdAt,
         List<String> comments,
         Long workspaceId
        ) {

    public TaskResponse(Task task) {
        this(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getWorkspace().getName(),
                task.getFileName(),
                task.getCreatedAt(),
                task.getComments().stream()
                        .map(Comments::getContent).toList(),
                task.getWorkspace().getId()

        );
    }
}
