package com.example.atlas.workspace.dto;

import com.example.atlas.task.dto.TaskResponse;
import com.example.atlas.users.dto.UserResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record WorkSpaceResponse
        (Long id, String name, List<UserResponse> users , @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime createdAt,
         List<TaskResponse> task)
{}
