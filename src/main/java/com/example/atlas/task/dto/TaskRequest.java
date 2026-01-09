package com.example.atlas.task.dto;

import com.example.atlas.task.TaskStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

}
