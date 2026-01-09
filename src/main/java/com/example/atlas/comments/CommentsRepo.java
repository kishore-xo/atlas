package com.example.atlas.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepo extends JpaRepository<Comments, Long > {
    List<Comments> findCommentsByTask_Id(Long taskId);
}
