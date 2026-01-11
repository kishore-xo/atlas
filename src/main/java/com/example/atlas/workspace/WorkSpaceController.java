package com.example.atlas.workspace;

import com.example.atlas.workspace.dto.WorkSpaceRequest;
import com.example.atlas.workspace.dto.WorkSpaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkSpaceController {

    private final WorkSpaceService workSpaceService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<WorkSpaceResponse>> getWorkSpaces(@PageableDefault(size = 5,sort = "id",direction = Sort.Direction.ASC)Pageable pageable){
        return new ResponseEntity<>(workSpaceService.getWorkSpaces(pageable), HttpStatus.OK);
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkSpaceResponse> getWorkSpaceById(@PathVariable(name = "workspaceId") Long id){
        return new ResponseEntity<>(workSpaceService.getWorkSpaceById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<WorkSpaceResponse> createWorkSpace(@RequestBody WorkSpaceRequest workSpaceRequest){
        return new ResponseEntity<>(workSpaceService.createWorkSpace(workSpaceRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{workspaceId}")
    public ResponseEntity<WorkSpaceResponse> updateWorkSpace(@PathVariable Long workspaceId, @RequestBody WorkSpaceRequest request){
        return new ResponseEntity<>(workSpaceService.updateWorkSpace(workspaceId, request), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Void> deleteWorkSpace(@PathVariable Long workspaceId){
        workSpaceService.deleteWorkSpace(workspaceId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{workspaceId}/users")
    public ResponseEntity<WorkSpaceResponse> addUserToWorkspace(@PathVariable Long workspaceId, @RequestParam String username){
        return new ResponseEntity<>(workSpaceService.addUserToWorkspace(workspaceId, username), HttpStatus.OK);
    }

    @DeleteMapping("/{workspaceId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromWorkspace(@PathVariable Long workspaceId, @PathVariable Long userId){
        workSpaceService.removeUserFromWorkspace(workspaceId, userId);
        return ResponseEntity.noContent().build();
    }
}
