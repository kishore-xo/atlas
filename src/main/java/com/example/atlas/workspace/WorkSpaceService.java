package com.example.atlas.workspace;


import com.example.atlas.exception.NotFoundException;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.Users;
import com.example.atlas.workspace.dto.WorkSpaceRequest;
import com.example.atlas.workspace.dto.WorkSpaceResponse;
import com.example.atlas.workspacemembers.WorkSpaceRole;
import com.example.atlas.workspacemembers.WorkspaceMemberRepo;
import com.example.atlas.workspacemembers.WorkspaceMembers;
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
    private final WorkspaceMemberRepo workspaceMemberRepo;

    public WorkSpaceResponse getWorkSpaceById(Long id) {
        Workspace workspace = workSpaceRepo.findById(id).orElseThrow(() -> new NotFoundException("WorkSpace not found: " + id));
        return new WorkSpaceResponse(workspace);
    }

    public List<WorkSpaceResponse> getWorkSpaces(Pageable pageable, String email) {
        Page<WorkSpaceResponse> workSpaceResponses = workSpaceRepo.findAllByWorkSpaceMembers_Users_Email(email, pageable)
                .map(WorkSpaceResponse::new);
        return workSpaceResponses.getContent();
    }

    public WorkSpaceResponse createWorkSpace(WorkSpaceRequest workSpaceRequest, String email) {

        Users user = userRepo.findUsersByEmail(email).orElseThrow(
                () -> new NotFoundException("User not found with email: " + email)
        );

        Workspace workspace = new Workspace();
        workspace.setName(workSpaceRequest.getName());
        Workspace saved=workSpaceRepo.save(workspace);

        WorkspaceMembers workspaceMembers = new WorkspaceMembers();
        workspaceMembers.setUsers(user);
        workspaceMembers.setWorkspace(workspace);
        workspaceMembers.setRole(WorkSpaceRole.OWNER);
        workspaceMemberRepo.save(workspaceMembers);

        return new WorkSpaceResponse(saved);
    }

    public WorkSpaceResponse updateWorkSpace(Long workspaceId, WorkSpaceRequest request) {
        Workspace workspace = workSpaceRepo.findById(workspaceId).orElseThrow(() -> new NotFoundException("Workspace not found: " + workspaceId));
        workspace.setName(request.getName());
        workSpaceRepo.save(workspace);
        return new WorkSpaceResponse(workspace);
    }

    public void deleteWorkSpace(Long workspaceId, String email) {
        Workspace workspace = workSpaceRepo.findById(workspaceId)
                .orElseThrow(() -> new NotFoundException("Workspace not found: " + workspaceId));

        Users user = userRepo.findUsersByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        WorkspaceMembers member = workspaceMemberRepo.findByWorkspaceIdAndUsersId(workspaceId, user.getId())
                .orElseThrow(() -> new NotFoundException("You are not a member of this workspace"));

        if (member.getRole() != WorkSpaceRole.OWNER) {
            throw new RuntimeException("Only the Owner can delete the workspace");
        }

        workSpaceRepo.delete(workspace);
    }
}
