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

@RequestMapping("/task/{id}/comment")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;

    @GetMapping()
    public ResponseEntity<List<CommentsResponse>> getComment(@PathVariable Long id){
        return new ResponseEntity<>(commentsService.getComment(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CommentsResponse> createComment(@PathVariable Long id, @Valid @RequestBody CommentsRequest commentsRequest){
        return new ResponseEntity<>(commentsService.createComment(id,commentsRequest),HttpStatus.CREATED);
    }
}
