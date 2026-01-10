package com.example.atlas.comments;

import com.example.atlas.comments.dto.CommentsRequest;
import com.example.atlas.comments.dto.CommentsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;

    @RequestMapping(path = "tasks/{taskId}/comments",method = RequestMethod.GET)
    public ResponseEntity<List<CommentsResponse>> getComment(@PathVariable(name = "taskId") Long id){
        return new ResponseEntity<>(commentsService.getComment(id), HttpStatus.OK);
    }

    @RequestMapping(path = "tasks/{taskId}/comments",method = RequestMethod.POST)
    public ResponseEntity<CommentsResponse> createComment(@PathVariable(name = "taskId") Long id, @Valid @RequestBody CommentsRequest commentsRequest){
        return new ResponseEntity<>(commentsService.createComment(id,commentsRequest),HttpStatus.CREATED);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentsResponse> getCommentById(@PathVariable Long commentId){
        return new ResponseEntity<>(commentsService.getCommentById(commentId), HttpStatus.OK);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentsResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentsRequest request){
        return new ResponseEntity<>(commentsService.updateComment(commentId, request), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId){
        commentsService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
