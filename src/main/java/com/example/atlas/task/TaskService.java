package com.example.atlas.task;


import com.example.atlas.comments.CommentsRepo;
import com.example.atlas.comments.dto.CommentsResponse;
import com.example.atlas.task.dto.TaskRequest;
import com.example.atlas.task.dto.TaskResponse;
import com.example.atlas.workspace.WorkSpaceRepo;
import com.example.atlas.workspace.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.atlas.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;
    private final WorkSpaceRepo workSpaceRepo;
    private final CommentsRepo commentsRepo;

    public List<TaskResponse> getTasks(Long id) {
        List<Task> tasks = taskRepo.findTasksByWorkspace_Id(id);

        return tasks.stream()
                .map(t -> {
                    List<CommentsResponse> commentsResponses = t.getComments().stream()
                            .map(c -> new CommentsResponse(
                                    c.getId(), c.getContent(), c.getCreatedAt(), c.getUsers().getId(), c.getTask().getId()
                            ))
                            .toList();

                    return new TaskResponse(
                            t.getId(),
                            t.getTitle(),
                            t.getDescription(),
                            t.getStatus(),
                            t.getWorkspace().getId(),
                            commentsResponses,
                            t.getCreatedAt()
                    );
                })
                .toList();
    }

    public TaskResponse createTask(Long id, TaskRequest taskRequest) {
        Workspace workspace = workSpaceRepo.findById(id).orElseThrow(() -> new NotFoundException("Workspace not exist with id " + id));
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());
        task.setWorkspace(workspace);

        taskRepo.save(task);
        return new TaskResponse(
                task.getId(), task.getTitle(), task.getDescription(),
                task.getStatus(), task.getWorkspace().getId(), Collections.emptyList(),
                task.getCreatedAt()
        );
    }






}
