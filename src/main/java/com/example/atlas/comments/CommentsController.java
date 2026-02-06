package com.example.atlas.comments;

import com.example.atlas.comments.dto.CommentsRequest;
import com.example.atlas.comments.dto.CommentsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentsController {

    private final CommentsService commentsService;

    @RequestMapping(path = "/tasks/{taskId}/comments",method = RequestMethod.GET)
    public ResponseEntity<List<CommentsResponse>> getComments(@PathVariable(name = "taskId") Long id, @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return new ResponseEntity<>(commentsService.getComments(id, pageable), HttpStatus.OK);
    }

    @RequestMapping(path = "/tasks/{taskId}/comments",method = RequestMethod.POST)
    public ResponseEntity<CommentsResponse> createComment(@PathVariable(name = "taskId") Long id, @Valid @RequestBody CommentsRequest commentsRequest, Principal principal){
        return new ResponseEntity<>(commentsService.createComment(id,commentsRequest,principal.getName()),HttpStatus.CREATED);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentsResponse> getCommentById(@PathVariable Long commentId){
        return new ResponseEntity<>(commentsService.getCommentById(commentId), HttpStatus.OK);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentsResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentsRequest request,Principal principal){
        return new ResponseEntity<>(commentsService.updateComment(commentId, request,principal.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,Principal principal){
        commentsService.deleteComment(commentId,principal.getName());
        return ResponseEntity.noContent().build();
    }
}
