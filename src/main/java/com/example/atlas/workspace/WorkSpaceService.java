package com.example.atlas.workspace;


import com.example.atlas.task.dto.TaskResponse;
import com.example.atlas.exception.NotFoundException;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.Users;
import com.example.atlas.workspace.dto.WorkSpaceRequest;
import com.example.atlas.workspace.dto.WorkSpaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class WorkSpaceService {

    private final WorkSpaceRepo workSpaceRepo;
    private final UserRepo userRepo;

    public WorkSpaceResponse getWorkSpaceById(Long id) {
        Workspace workspace = workSpaceRepo.findById(id).orElseThrow(() -> new NotFoundException("WorkSpace not found: " + id));
        return workToResponse(workspace);
    }

    public List<WorkSpaceResponse> getWorkSpaces() {
        List<Workspace> workspaces = workSpaceRepo.findAll();
        return workspaces.stream()
                .map(this::workToResponse)
                .toList();
    }

    public WorkSpaceResponse createWorkSpace(WorkSpaceRequest workSpaceRequest) {
        Users users = userRepo.findUsersByName(workSpaceRequest.getUserName()).orElseThrow(
                () -> new NotFoundException("User not found: " + workSpaceRequest.getUserName())
        );
        Workspace workspace = new Workspace();
        workspace.setName(workSpaceRequest.getName());
        workspace.setUsers(users);
        workSpaceRepo.save(workspace);
        return workToResponse(workspace);
    }


    private WorkSpaceResponse workToResponse(Workspace workspace){
        List<TaskResponse> taskResponses = workspace.getTasks().stream()
                .map(t -> new TaskResponse(
                        t.getId(),
                        t.getTitle(),
                        t.getDescription(),
                        t.getStatus(),
                        workspace.getId(),
                        Collections.emptyList(),
                        t.getCreatedAt()
                ))
                .toList();

        return new WorkSpaceResponse(workspace.getId(), workspace.getName(), workspace.getUsers(), workspace.getCreatedAt(), taskResponses);
    }
}
