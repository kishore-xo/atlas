package com.example.atlas.workspace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkSpaceRepo extends JpaRepository<Workspace,Long> {
}
