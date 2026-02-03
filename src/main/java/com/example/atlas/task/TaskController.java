package com.example.atlas.task;

import com.example.atlas.task.dto.TaskRequest;
import com.example.atlas.task.dto.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @RequestMapping(path = "workspaces/{workspaceId}/tasks", method = RequestMethod.GET)
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable Long workspaceId, @PageableDefault(size = 5, direction = Sort.Direction.ASC, sort = "id") Pageable pageable) {
        return new ResponseEntity<>(taskService.getTasks(workspaceId, pageable), HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @RequestMapping(path = "workspaces/{workspaceId}/tasks", method = RequestMethod.POST)
    public ResponseEntity<TaskResponse> createTask(@PathVariable Long workspaceId, @Valid @RequestBody TaskRequest taskRequest) {
        return new ResponseEntity<>(taskService.createTask(workspaceId, taskRequest), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/tasks/{taskId}", method = RequestMethod.GET)
    public ResponseEntity<TaskResponse> getTask(@PathVariable(name = "taskId") Long id) {
        return new ResponseEntity<>(taskService.getTask(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId, @RequestBody TaskRequest request) {
        return new ResponseEntity<>(taskService.updateTask(taskId, request), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/tasks/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long taskId, @RequestParam TaskStatus status) {
        return new ResponseEntity<>(taskService.updateStatus(taskId, status), HttpStatus.OK);
    }

    @PostMapping("/tasks/{taskId}/file")
    public ResponseEntity<TaskResponse> fileUpload(@RequestParam MultipartFile file, @PathVariable Long taskId) {
        TaskResponse taskResponse = taskService.fileUpload(file, taskId);
        return ResponseEntity.ok(taskResponse);
    }
}
