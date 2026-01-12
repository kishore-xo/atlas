package com.example.atlas.workspace.dto;

import com.example.atlas.task.dto.TaskResponse;
import com.example.atlas.users.dto.UserResponse;
import com.example.atlas.workspace.Workspace;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record WorkSpaceResponse
        (Long id, String name, List<UserResponse> users,
         @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime createdAt,
         List<TaskResponse> task) {

    public WorkSpaceResponse(Workspace workspace) {
        this(
                workspace.getId(),
                workspace.getName(),
                workspace.getUsers().stream()
                        .map(UserResponse::new).toList(),
                workspace.getCreatedAt(),
                workspace.getTasks().stream().map(TaskResponse::new).toList()
        );
    }
}
