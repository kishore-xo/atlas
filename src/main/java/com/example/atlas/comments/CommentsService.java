package com.example.atlas.comments;

import com.example.atlas.comments.dto.CommentsRequest;
import com.example.atlas.comments.dto.CommentsResponse;
import com.example.atlas.task.Task;
import com.example.atlas.task.TaskRepo;
import com.example.atlas.exception.NotFoundException;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentsService {
    private final TaskRepo taskRepo;
    private final CommentsRepo commentsRepo;
    private final UserRepo userRepo;

    public List<CommentsResponse> getComment(Long id) {
        List<Comments> commentsList = commentsRepo.findCommentsByTask_Id(id);
        return commentsList.stream()
                .map(list -> new CommentsResponse(
                        list.getId(), list.getContent(),
                        list.getCreatedAt(), list.getUsers().getId(),
                        list.getTask().getId()
                )).toList();
    }

    public CommentsResponse createComment(Long id, @NonNull CommentsRequest commentsRequest) {
        Task task = taskRepo.findById(id).orElseThrow(() -> new NotFoundException("Task not found: " + id));
        Users users = userRepo.findUsersByName(commentsRequest.getUsername()).orElseThrow(() -> new NotFoundException("User not found: " + commentsRequest.getUsername()));
        Comments comments = new Comments();
        comments.setContent(commentsRequest.getContent());
        comments.setTask(task);
        comments.setUsers(users);
        commentsRepo.save(comments);
        return commentToDto(comments);
    }

    public CommentsResponse getCommentById(Long id){
        Comments comments = commentsRepo.findById(id).orElseThrow(() -> new NotFoundException("Comment not found: " + id));
        return commentToDto(comments);
    }

    public CommentsResponse updateComment(Long id, CommentsRequest request){
        Comments comments = commentsRepo.findById(id).orElseThrow(() -> new NotFoundException("Comment not found: " + id));
        comments.setContent(request.getContent());
        commentsRepo.save(comments);
        return commentToDto(comments);
    }

    public void deleteComment(Long id){
        Comments comments = commentsRepo.findById(id).orElseThrow(() -> new NotFoundException("Comment not found: " + id));
        commentsRepo.delete(comments);
    }

    public CommentsResponse commentToDto(Comments comments) {
        return new CommentsResponse(
                comments.getId(), comments.getContent(), comments.getCreatedAt(),
                comments.getUsers().getId(), comments.getTask().getId()
        );
    }
}
