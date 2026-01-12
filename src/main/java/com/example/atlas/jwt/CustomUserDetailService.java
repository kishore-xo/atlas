package com.example.atlas.jwt;

import com.example.atlas.exception.NotFoundException;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.Users;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailService implements UserDetailsService  {
    private final UserRepo userRepo;

    public CustomUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        Users users =
                userRepo.findUsersByEmail(email).orElseThrow(
                        () -> new NotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(users.getEmail())
                .password(users.getPassword())
                .authorities("ROLE_" + users.getRole())
                .build();
    }
}
