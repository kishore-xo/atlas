package com.example.atlas.task;

import com.example.atlas.task.dto.TaskRequest;
import com.example.atlas.task.dto.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @RequestMapping(path = "workspaces/{workspaceId}/tasks",method = RequestMethod.GET)
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable Long workspaceId){
        return new ResponseEntity<>(taskService.getTasks(workspaceId), HttpStatus.OK);

    }


    @RequestMapping(path = "workspaces/{workspaceId}/tasks",method = RequestMethod.POST)
    public ResponseEntity<TaskResponse> createTask(@PathVariable Long workspaceId, @Valid @RequestBody TaskRequest taskRequest) {
        return new ResponseEntity<>(taskService.createTask(workspaceId,taskRequest),HttpStatus.CREATED);
    }

    @RequestMapping(path = "/tasks/{taskId}",method = RequestMethod.GET)
    public ResponseEntity<TaskResponse> getTask(@PathVariable(name = "taskId")Long id){
        return new ResponseEntity<>(taskService.getTask(id),HttpStatus.OK);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId, @RequestBody TaskRequest request){
        return new ResponseEntity<>(taskService.updateTask(taskId, request), HttpStatus.OK);
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId){
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/tasks/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long taskId, @RequestBody TaskRequest request){
        return new ResponseEntity<>(taskService.updateStatus(taskId, request.getStatus()), HttpStatus.OK);
    }

}
