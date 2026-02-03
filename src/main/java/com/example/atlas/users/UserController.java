package com.example.atlas.users;


import com.example.atlas.users.dto.UserRequest;
import com.example.atlas.users.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<UserResponse>> getUsers(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.getByPage(pageable));
    }

    @PostAuthorize("returnObject.body.name() == authentication.name or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUser(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(service.createUser(userRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable long id, @Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(service.updateUser(id, userRequest), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/role/{userId}")
    public ResponseEntity<UserResponse> updateRole(@PathVariable long userId) {
        return new ResponseEntity<>(service.updateRole(userId), HttpStatus.ACCEPTED);
    }

    @PostMapping("/profile-image")
    public ResponseEntity<UserResponse> profileUpdate(@RequestParam(name = "file") MultipartFile file, Principal principal) {
        return ResponseEntity.ok(service.profileUpdate(file, principal.getName()));
    }

    @GetMapping("/profile-image")
    public ResponseEntity<Object> profileImage(Principal principal) throws MalformedURLException {
        Object profileImage = service.profileImage(principal.getName());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(profileImage);
    }
}
