package com.example.atlas.task;


import com.example.atlas.task.dto.TaskRequest;
import com.example.atlas.task.dto.TaskResponse;
import com.example.atlas.workspace.WorkSpaceRepo;
import com.example.atlas.workspace.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.atlas.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;
    private final WorkSpaceRepo workSpaceRepo;

    public List<TaskResponse> getTasks(Long id,Pageable pageable) {
        return taskRepo.findTasksByWorkspace_Id(id, pageable).stream()
                .map(TaskResponse::new).toList();
    }

    public TaskResponse createTask(Long id, TaskRequest taskRequest) {
        Workspace workspace = workSpaceRepo.findById(id).orElseThrow(() -> new NotFoundException("Workspace not exist with id " + id));
        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());
        task.setWorkspace(workspace);

        taskRepo.save(task);
        return new TaskResponse(task);
    }


    public TaskResponse getTask(Long id) {

        Task task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id: " + id));
        return new TaskResponse(task);
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id: " + id));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        taskRepo.save(task);
        return new TaskResponse(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id: " + id));
        taskRepo.delete(task);
    }

    public TaskResponse updateStatus(Long id, TaskStatus status) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id: " + id));
        task.setStatus(status);
        taskRepo.save(task);
        return new TaskResponse(task);
    }

}
