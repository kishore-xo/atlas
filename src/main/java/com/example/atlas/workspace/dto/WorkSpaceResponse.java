package com.example.atlas.workspace.dto;

import com.example.atlas.task.dto.TaskResponse;
import com.example.atlas.users.Users;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record WorkSpaceResponse
        (Long id, String name, Users userName , @JsonFormat(pattern = "dd-MM-yyy HH:mm:ss")LocalDateTime createdAt,
         List<TaskResponse> task)
{}
