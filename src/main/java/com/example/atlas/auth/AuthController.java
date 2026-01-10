package com.example.atlas.auth;

import com.example.atlas.auth.dto.AuthRequest;
import com.example.atlas.users.dto.UserRequest;
import com.example.atlas.users.dto.UserResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRequest userRequest) {
        return authService.register(userRequest);
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest){
        return authService.login(authRequest);
    }


}
