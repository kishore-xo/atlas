package com.example.atlas.workspace;


import com.example.atlas.task.TaskService;
import com.example.atlas.task.dto.TaskResponse;
import com.example.atlas.exception.NotFoundException;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.UserService;
import com.example.atlas.users.Users;
import com.example.atlas.users.dto.UserResponse;
import com.example.atlas.workspace.dto.WorkSpaceRequest;
import com.example.atlas.workspace.dto.WorkSpaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkSpaceService {

    private final WorkSpaceRepo workSpaceRepo;
    private final UserRepo userRepo;
    private final TaskService taskService;
    private final UserService userService;

    public WorkSpaceResponse getWorkSpaceById(Long id) {
        Workspace workspace = workSpaceRepo.findById(id).orElseThrow(() -> new NotFoundException("WorkSpace not found: " + id));
        return workToResponse(workspace);
    }

    public List<WorkSpaceResponse> getWorkSpaces(Pageable pageable) {
        Page<WorkSpaceResponse> workSpaceResponses =workSpaceRepo.findAll(pageable)
                .map(this::workToResponse);
        return workSpaceResponses.getContent();
    }

    public WorkSpaceResponse createWorkSpace(WorkSpaceRequest workSpaceRequest) {
        Users user = userRepo.findUsersByName(workSpaceRequest.getUserName()).orElseThrow(
                () -> new NotFoundException("User not found: " + workSpaceRequest.getUserName())
        );
        Workspace workspace = new Workspace();
        workspace.setName(workSpaceRequest.getName());
        Workspace saved = workSpaceRepo.save(workspace);
        saved.getUsers().add(user);
        user.getWorkspaces().add(saved);
        userRepo.save(user);
        return workToResponse(saved);
    }

    public WorkSpaceResponse addUserToWorkspace(Long workspaceId, String userName){
        Workspace workspace = workSpaceRepo.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace not found: " + workspaceId));
        Users user = userRepo.findUsersByName(userName).orElseThrow(() -> new NotFoundException("User not found: " + userName));
        if (!workspace.getUsers().contains(user)){
            workspace.getUsers().add(user);
            user.getWorkspaces().add(workspace);
            userRepo.save(user);
            workSpaceRepo.save(workspace);
        }
        return workToResponse(workspace);
    }

    public void removeUserFromWorkspace(Long workspaceId, Long userId){
        Workspace workspace = workSpaceRepo.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace not found: " + workspaceId));
        Users user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("User not found: " + userId));
        workspace.getUsers().removeIf(u -> u.getId().equals(user.getId()));
        // remove workspace from owning side and save user
        user.getWorkspaces().removeIf(w -> w.getId().equals(workspace.getId()));
        userRepo.save(user);
        workSpaceRepo.save(workspace);
    }

    public WorkSpaceResponse updateWorkSpace(Long workspaceId, WorkSpaceRequest request){
        Workspace workspace = workSpaceRepo.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace not found: " + workspaceId));
        workspace.setName(request.getName());
        workSpaceRepo.save(workspace);
        return workToResponse(workspace);
    }

    public void deleteWorkSpace(Long workspaceId){
        Workspace workspace = workSpaceRepo.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace not found: " + workspaceId));
        workspace.getUsers().forEach(
                users -> users.getWorkspaces().remove(workspace)
        );
        workSpaceRepo.delete(workspace);
    }


    private WorkSpaceResponse workToResponse(Workspace workspace){
        List<TaskResponse> taskResponses = workspace.getTasks().stream()
                .map(taskService::taskToResponse).toList();

        List<UserResponse> userResponses = workspace.getUsers().stream()
                .map(userService::userToResponse)
                .toList();

        return new WorkSpaceResponse(workspace.getId(), workspace.getName(), userResponses,workspace.getCreatedAt(),taskResponses);
    }
}
