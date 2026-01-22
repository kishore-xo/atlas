package com.example.atlas.workspacemembers;

import com.example.atlas.exception.BadRequestException;
import com.example.atlas.exception.ForbiddenException;
import com.example.atlas.exception.NotFoundException;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.Users;
import com.example.atlas.workspace.WorkSpaceRepo;
import com.example.atlas.workspace.Workspace;
import com.example.atlas.workspacemembers.dto.AddMemberRequest;
import com.example.atlas.workspacemembers.dto.WorkspaceMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkspaceMemberService {

    private final WorkspaceMemberRepo memberRepo;
    private final WorkSpaceRepo workSpaceRepo;
    private final UserRepo userRepo;

    public List<WorkspaceMemberResponse> getMembers(Long workspaceId) {
        if (!workSpaceRepo.existsById(workspaceId)) {
            throw new NotFoundException("Workspace not found");
        }
        return memberRepo.findByWorkspaceId(workspaceId).stream()
                .map(WorkspaceMemberResponse::new)
                .toList();
    }

    public WorkspaceMemberResponse addMember(Long workspaceId, AddMemberRequest request, String currentUserEmail) {
        Workspace workspace = workSpaceRepo.findById(workspaceId)
                .orElseThrow(() -> new NotFoundException("Workspace not found"));

        Long currentUserId = userRepo.findUsersIdByEmail(currentUserEmail);

        // Check if requester has permission (OWNER or ADMIN)
        WorkspaceMembers requesterMember = memberRepo.findByWorkspaceIdAndUsersId(workspaceId, currentUserId)
                .orElseThrow(() -> new ForbiddenException("You are not a member of this workspace"));

        if (requesterMember.getRole() == WorkSpaceRole.MEMBER) {
            throw new ForbiddenException("Only Admins and Owners can add members");
        }

        Users newUser = userRepo.findUsersByEmail(request.email())
                .orElseThrow(() -> new NotFoundException("User to add not found"));

        if (memberRepo.existsByWorkspaceIdAndUsersId(workspaceId, newUser.getId())) {
            throw new BadRequestException("User is already a member of this workspace");
        }

        WorkspaceMembers newMember = new WorkspaceMembers();
        newMember.setWorkspace(workspace);
        newMember.setUsers(newUser);
        newMember.setRole(request.workSpaceRole() != null ? request.workSpaceRole() : WorkSpaceRole.MEMBER);

        return new WorkspaceMemberResponse(memberRepo.save(newMember));
    }

    @CacheEvict(value = "member", key = "#workspaceId")
    public void removeMember(Long workspaceId, Long userId, String currentUserEmail) {
        Users requester = userRepo.findUsersByEmail(currentUserEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        WorkspaceMembers requesterMember = memberRepo.findByWorkspaceIdAndUsersId(workspaceId, requester.getId())
                .orElseThrow(() -> new ForbiddenException("You are not a member of this workspace"));

        // Self-removal (Leave Workspace)
        if (requester.getId().equals(userId)) {
            memberRepo.deleteByWorkspaceIdAndUsersId(workspaceId, userId);
            return;
        }

        // Removing others
        if (requesterMember.getRole() == WorkSpaceRole.MEMBER) {
            throw new ForbiddenException("You don't have permission to remove members");
        }

        // fetch target member
        WorkspaceMembers targetMember = memberRepo.findByWorkspaceIdAndUsersId(workspaceId, userId)
                .orElseThrow(() -> new NotFoundException("Member not found"));

        // Prevent removing Owner
        if (targetMember.getRole() == WorkSpaceRole.OWNER) {
            throw new ForbiddenException("Cannot remove the Owner of the workspace");
        }

        memberRepo.delete(targetMember);
    }

    @CachePut(value = "member", key = "#workspaceId")
    public WorkspaceMemberResponse updateMemberRole(Long workspaceId, Long userId, WorkSpaceRole newRole, String currentUserEmail) {
        Users requester = userRepo.findUsersByEmail(currentUserEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        WorkspaceMembers requesterMember = memberRepo.findByWorkspaceIdAndUsersId(workspaceId, requester.getId())
                .orElseThrow(() -> new ForbiddenException("You are not a member of this workspace"));

        if (requesterMember.getRole() != WorkSpaceRole.OWNER && requesterMember.getRole() != WorkSpaceRole.ADMIN) {
            throw new ForbiddenException("Insufficient permissions");
        }

        // Only Owner can change others to/from Owner? Let's say only Owner can make Admins for simplicity, 
        // or Admin can make other Admins.
        // Let's stick to: Owner > Admin > Member.

        WorkspaceMembers targetMember = memberRepo.findByWorkspaceIdAndUsersId(workspaceId, userId)
                .orElseThrow(() -> new NotFoundException("Member not found"));

        targetMember.setRole(newRole);
        return new WorkspaceMemberResponse(memberRepo.save(targetMember));
    }

    public boolean isMember(Long id, String email) {
        return memberRepo.existsByWorkspaceIdAndUsers_Email(id, email);
    }
}
