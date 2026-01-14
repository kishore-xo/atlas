package com.example.atlas.users;

import com.example.atlas.comments.Comments;
import com.example.atlas.workspacemembers.WorkspaceMembers;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<WorkspaceMembers> workspaceMembers;

    @OneToMany(mappedBy = "users", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comments> comments = new ArrayList<>();


}
