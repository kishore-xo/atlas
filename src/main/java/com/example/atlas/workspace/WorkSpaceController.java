package com.example.atlas.workspace;

import com.example.atlas.workspace.dto.WorkSpaceRequest;
import com.example.atlas.workspace.dto.WorkSpaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work")
@RequiredArgsConstructor
public class WorkSpaceController {

    private final WorkSpaceService workSpaceService;

    @GetMapping()
    public ResponseEntity<List<WorkSpaceResponse>> getWorkSpaces(){
        return new ResponseEntity<>(workSpaceService.getWorkSpaces(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkSpaceResponse> getWorkSpaceById(@PathVariable Long id){
        return new ResponseEntity<>(workSpaceService.getWorkSpaceById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<WorkSpaceResponse> createWorkSpace(@RequestBody WorkSpaceRequest workSpaceRequest){
        return new ResponseEntity<>(workSpaceService.createWorkSpace(workSpaceRequest), HttpStatus.CREATED);
    }
}
