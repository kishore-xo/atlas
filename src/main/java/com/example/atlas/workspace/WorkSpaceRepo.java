package com.example.atlas.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkSpaceRepo extends JpaRepository<Workspace,Long> {
    Page<Workspace> findAllByWorkSpaceMembers_Users_Email(String email, Pageable pageable);
}
