package com.example.atlas.users;


import com.example.atlas.exception.NotFoundException;
import com.example.atlas.users.dto.UserRequest;
import com.example.atlas.users.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;


    public List<UserResponse> getUsers() {
        List<Users> list = repo.findAll();
        return list.stream()
                .map(l -> new UserResponse(
                        l.getId(), l.getName(), l.getEmail(),
                        l.getPassword(), l.getCreatedAt()
                )).toList();
    }


    public List<UserResponse> getByPage(Pageable pageable) {
        Page<UserResponse> userResponses = repo.findAll(pageable)
                .map(l -> new UserResponse(
                        l.getId(), l.getName(), l.getEmail(),
                        l.getPassword(), l.getCreatedAt()
                ));
        return userResponses.getContent();

    }

    public UserResponse getUser(Long id) {
        Users us = repo.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
        return userToResponse(us);
    }

    public UserResponse createUser(UserRequest userRequest) {
        Users users = new Users();
        users.setName(userRequest.getName());
        users.setEmail(userRequest.getEmail());
        users.setPassword(userRequest.getPassword());

        repo.save(users);
        return userToResponse(users);
    }

    public UserResponse deleteUser(Long id) {
        Users users = repo.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
        repo.delete(users);
        return userToResponse(users);
    }

    public UserResponse updateUser(Long id, UserRequest userRequest) {
        Users users = repo.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
        users.setName(userRequest.getName());
        users.setEmail(userRequest.getEmail());
        users.setPassword(userRequest.getPassword());
        repo.save(users);
        return userToResponse(users);
    }

    private UserResponse userToResponse(Users us) {
        return new UserResponse(us.getId(), us.getName(), us.getEmail(), us.getPassword(), us.getCreatedAt());
    }

}
