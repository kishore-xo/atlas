package com.example.atlas.users;


import com.example.atlas.exception.NotFoundException;
import com.example.atlas.users.dto.UserRequest;
import com.example.atlas.users.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;
    private final BCryptPasswordEncoder encoder;


    public List<UserResponse> getByPage(Pageable pageable) {
        Page<UserResponse> userResponses = repo.findAll(pageable)
                .map(UserResponse::new);
        return userResponses.getContent();

    }

    @Cacheable(value = "user",key = "#id")
    public UserResponse getUser(Long id) {
        return repo.findById(id)
                .map(UserResponse::new)
                .orElseThrow(()-> new NotFoundException("User not found with id: "+id));
    }

    public UserResponse createUser(UserRequest userRequest) {
        Users users = new Users();
        users.setName(userRequest.getName());
        users.setEmail(userRequest.getEmail());
        users.setRole(Role.USER);
        users.setPassword(encoder.encode(userRequest.getPassword()));

        repo.save(users);
        return new UserResponse(users);
    }

    @CacheEvict(value = "user",key = "#id")
    public void deleteUser(Long id) {
        Users users = repo.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
        repo.delete(users);
    }

    @CachePut(value = "user",key = "#id")
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        Users users = repo.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
        users.setName(userRequest.getName());
        users.setEmail(userRequest.getEmail());
        users.setPassword(encoder.encode(userRequest.getPassword()));
        repo.save(users);
        return new UserResponse(users);
    }

    @CachePut(value = "user",key = "#userId")
    public UserResponse updateRole(long userId) {
        Users user = repo.findById(userId).orElseThrow(() -> new NotFoundException("User not found: " + userId));
        user.setRole(Role.ADMIN);
        repo.save(user);
        return new UserResponse(user);
    }
}
