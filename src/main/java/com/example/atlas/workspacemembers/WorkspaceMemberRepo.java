package com.example.atlas.workspacemembers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceMemberRepo extends JpaRepository<WorkspaceMembers,Long> {
    Optional<WorkspaceMembers> findByWorkspaceIdAndUsersId(Long workspaceId,Long userId);

    void deleteByWorkspaceIdAndUsersId(Long workspaceId, Long usersId);

    boolean existsByWorkspaceIdAndUsersId(Long workspaceId, Long usersId);

    List<WorkspaceMembers> findByWorkspaceId(Long workspaceId);
}
