package com.example.atlas.workspace;

import com.example.atlas.task.Task;
import com.example.atlas.users.Users;
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
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "workspaces")
    private List<Users> users = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "workspace", orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

}
