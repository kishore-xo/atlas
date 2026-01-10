package com.example.atlas.auth;

import com.example.atlas.auth.dto.AuthRequest;
import com.example.atlas.exception.BadRequestException;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.Users;
import com.example.atlas.users.UserService;
import com.example.atlas.users.dto.UserRequest;
import com.example.atlas.users.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;

    public UserResponse register(UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    public String login(AuthRequest request){
        Users user = userRepo.findUsersByEmail(request.getEmail()).orElseThrow(() -> new BadRequestException("Invalid credentials"));
        if (!encoder.matches(request.getPassword(), user.getPassword())){
            throw new BadRequestException("Invalid credentials");
        }
        // placeholder token
        return UUID.randomUUID().toString();
    }
}
