package com.example.atlas.auth;

import com.example.atlas.users.Role;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.Users;
import com.example.atlas.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = token.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        log.info("OAuth2 login success for email: {}", email);

        Users user = userRepo.findUsersByEmail(email).orElseGet(() -> {
            log.info("Creating new user for email: {}", email);
            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setRole(Role.USER);
            newUser.setPassword(UUID.randomUUID().toString());
            return userRepo.save(newUser);
        });

        String jwt = jwtUtil.generateToken(user);
        String redirectUrl = "http://localhost:3000/oauth-callback?token=" + jwt;
        
        response.sendRedirect(redirectUrl);
    }
}
