package com.example.atlas.comments;

import com.example.atlas.comments.dto.CommentsRequest;
import com.example.atlas.comments.dto.CommentsResponse;
import com.example.atlas.exception.ForbiddenException;
import com.example.atlas.task.Task;
import com.example.atlas.task.TaskRepo;
import com.example.atlas.exception.NotFoundException;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentsService {
    private final TaskRepo taskRepo;
    private final CommentsRepo commentsRepo;
    private final UserRepo userRepo;

    public List<CommentsResponse> getComments(Long id) {
        return commentsRepo.findCommentsByTask_Id(id).stream()
                .map((CommentsResponse::new))
                .toList();

    }

    public CommentsResponse createComment(Long id, CommentsRequest commentsRequest,String email) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task not found: " + id));
        Users users = userRepo.findUsersByEmail(email).orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        Comments comments = new Comments();
        comments.setContent(commentsRequest.getContent());
        comments.setTask(task);
        comments.setUsers(users);
        commentsRepo.save(comments);
        return new CommentsResponse(comments);
    }

    @Cacheable(value = "comment",key = "#id")
    public CommentsResponse getCommentById(Long id) {
        Comments comments = commentsRepo.findById(id).orElseThrow(() -> new NotFoundException("Comment not found: " + id));
        return new CommentsResponse(comments);
    }

    @CachePut(value = "comment",key = "#id")
    public CommentsResponse updateComment(Long id, CommentsRequest request,String email) {
        Comments comments = commentsRepo.findById(id).orElseThrow(() -> new NotFoundException("Comment not found: " + id));
        if(!comments.getUsers().getEmail().equals(email)){
            throw new ForbiddenException("It's not your comment");
        }
        comments.setContent(request.getContent());
        commentsRepo.save(comments);
        return new CommentsResponse(comments);
    }

    @CacheEvict(value = "comment",key = "#id")
    public void deleteComment(Long id,String email) {
        Comments comments = commentsRepo.findById(id).orElseThrow(() -> new NotFoundException("Comment not found: " + id));
        if(!comments.getUsers().getEmail().equals(email)){
            throw new ForbiddenException("It's not your comment");
        }
        commentsRepo.delete(comments);
    }

}
