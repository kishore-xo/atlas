package com.example.atlas.workspace;

import com.example.atlas.workspace.dto.WorkSpaceRequest;
import com.example.atlas.workspace.dto.WorkSpaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/workspaces")
@RequiredArgsConstructor
public class WorkSpaceController {

    private final WorkSpaceService workSpaceService;

    @GetMapping()
    public ResponseEntity<List<WorkSpaceResponse>> getWorkSpaces(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            Principal principal
    ) {
        return new ResponseEntity<>(workSpaceService.getWorkSpaces(pageable, principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkSpaceResponse> getWorkSpaceById(@PathVariable(name = "workspaceId") Long id, Principal principal) {
        return new ResponseEntity<>(workSpaceService.getWorkSpaceById(id, principal.getName()), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<WorkSpaceResponse> createWorkSpace(@RequestBody WorkSpaceRequest workSpaceRequest, Principal principal) {
        return new ResponseEntity<>(workSpaceService.createWorkSpace(workSpaceRequest, principal.getName()), HttpStatus.CREATED);
    }

    @PutMapping("/{workspaceId}")
    public ResponseEntity<WorkSpaceResponse> updateWorkSpace(@PathVariable Long workspaceId, @RequestBody WorkSpaceRequest request) {
        return new ResponseEntity<>(workSpaceService.updateWorkSpace(workspaceId, request), HttpStatus.OK);
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Void> deleteWorkSpace(@PathVariable Long workspaceId, Principal principal) {
        workSpaceService.deleteWorkSpace(workspaceId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
