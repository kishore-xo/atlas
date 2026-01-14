package com.example.atlas.workspacemembers.dto;

import com.example.atlas.workspacemembers.WorkSpaceRole;

public record AddMemberRequest(Long userId, WorkSpaceRole workSpaceRole) {
}
