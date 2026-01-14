package com.example.atlas.workspacemembers.dto;


import com.example.atlas.workspacemembers.WorkSpaceRole;
import com.example.atlas.workspacemembers.WorkspaceMembers;

import java.time.LocalDateTime;

public record WorkspaceMemberResponse
        (
                Long id, Long userId, String username,
                String workspaceName, WorkSpaceRole role,
                LocalDateTime createdAt
        ) {
    public WorkspaceMemberResponse(WorkspaceMembers workspaceMembers) {
        this(
                workspaceMembers.getId(), workspaceMembers.getUsers().getId(), workspaceMembers.getUsers().getName(),
                workspaceMembers.getWorkspace().getName(), workspaceMembers.getRole(),
                workspaceMembers.getCreatedAt()
        );
    }
}
