package com.example.atlas.workspace.dto;

import com.example.atlas.task.Task;
import com.example.atlas.users.Users;
import com.example.atlas.workspace.Workspace;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record WorkSpaceResponse
        (Long id, String name,
         @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime createdAt,
         List<String> usernames,
         List<String> tasknames) {

    public WorkSpaceResponse(Workspace workspace) {
        this(
                workspace.getId(),
                workspace.getName(),
                workspace.getCreatedAt(),
                workspace.getUsers().stream()
                        .map(Users::getName).toList(),
                workspace.getTasks().stream()
                        .map(Task::getTitle).toList()
        );
    }
}
