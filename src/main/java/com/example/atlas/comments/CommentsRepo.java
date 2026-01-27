package com.example.atlas.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CommentsRepo extends JpaRepository<Comments, Long > {
    Page<Comments> findCommentsByTask_Id(Long taskId, Pageable pageable);
}
