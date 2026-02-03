package com.example.atlas.task;


import com.example.atlas.task.dto.TaskRequest;
import com.example.atlas.task.dto.TaskResponse;
import com.example.atlas.workspace.WorkSpaceRepo;
import com.example.atlas.workspace.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.atlas.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepo taskRepo;
    private final WorkSpaceRepo workSpaceRepo;
    private final String filePath = "src/main/resources/uploads/taskfiles";

    public List<TaskResponse> getTasks(Long id, Pageable pageable) {
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

    @Cacheable(value = "task", key = "#id")
    public TaskResponse getTask(Long id) {

        Task task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id: " + id));
        return new TaskResponse(task);
    }

    @CachePut(value = "task", key = "#id")
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id: " + id));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        taskRepo.save(task);
        return new TaskResponse(task);
    }

    @CacheEvict(value = "task", key = "#id")
    public void deleteTask(Long id) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id: " + id));
        taskRepo.delete(task);
    }

    @CachePut(value = "task", key = "#id")
    public TaskResponse updateStatus(Long id, TaskStatus status) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task not found with id: " + id));
        task.setStatus(status);
        taskRepo.save(task);
        return new TaskResponse(task);
    }

    public TaskResponse fileUpload(MultipartFile file, Long taskId) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        Task task = taskRepo.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found with id: " + taskId));

        String filename = task.getTitle() + ".pdf";

        try {
            Path path = Path.of(filePath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Files.copy(file.getInputStream(), path.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            task.setFileName(filename);
            taskRepo.save(task);
            return new TaskResponse(task);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
