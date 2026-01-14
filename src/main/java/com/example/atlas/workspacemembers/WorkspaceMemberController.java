package com.example.atlas.workspacemembers;

import com.example.atlas.workspacemembers.dto.AddMemberRequest;
import com.example.atlas.workspacemembers.dto.WorkspaceMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/workspaces/{workspaceId}/members")
@RequiredArgsConstructor
public class WorkspaceMemberController {

    private final WorkspaceMemberService memberService;

    @GetMapping
    public ResponseEntity<List<WorkspaceMemberResponse>> getMembers(@PathVariable Long workspaceId) {
        return ResponseEntity.ok(memberService.getMembers(workspaceId));
    }

    @PostMapping
    public ResponseEntity<WorkspaceMemberResponse> addMember(
            @PathVariable Long workspaceId,
            @RequestBody AddMemberRequest request,
            Principal principal
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberService.addMember(workspaceId, request, principal.getName()));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long workspaceId,
            @PathVariable Long userId,
            Principal principal
    ) {
        memberService.removeMember(workspaceId, userId, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<WorkspaceMemberResponse> updateMemberRole(
            @PathVariable Long workspaceId,
            @PathVariable Long userId,
            @RequestParam WorkSpaceRole role,
            Principal principal
    ) {
        return ResponseEntity.ok(memberService.updateMemberRole(workspaceId, userId, role, principal.getName()));
    }
}
