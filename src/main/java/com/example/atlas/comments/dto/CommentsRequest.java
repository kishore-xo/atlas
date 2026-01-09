package com.example.atlas.comments.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CommentsRequest {
    @NonNull
    private String content;

    private String username;
}
