package com.example.atlas.task.dto;

import com.example.atlas.task.TaskStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    @NotNull
    private String title;
    @NotNull
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

}
