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
@RequestMapping("work/{id}/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping()
    public ResponseEntity<List<TaskResponse>> getTasks(@PathVariable Long id){
        return new ResponseEntity<>(taskService.getTasks(id), HttpStatus.OK);

    }


    @PostMapping()
    public ResponseEntity<TaskResponse> createTask(@PathVariable Long id,@Valid @RequestBody TaskRequest taskRequest) {
        return new ResponseEntity<>(taskService.createTask(id,taskRequest),HttpStatus.CREATED);
    }

}
