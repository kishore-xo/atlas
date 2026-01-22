package com.example.atlas.config;

import com.example.atlas.filter.RateLimitFilter;
import com.example.atlas.filter.JwtFilter;
import com.example.atlas.users.Role;
import com.example.atlas.users.UserRepo;
import com.example.atlas.users.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

    private final JwtFilter jwtFilter;
    private final RateLimitFilter rateLimitFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

        return security.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/auth/**", "/swagger-ui/**",
                                                "/swagger-ui/index.html", "/v3/api-docs/**").permitAll()
                                        .anyRequest().authenticated()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


    @Bean
    CommandLineRunner inject(UserRepo userRepo) {
        return args -> {
            if (userRepo.findUsersByEmail("admin@gmail.com").isEmpty()) {
                Users admin = new Users();
                admin.setName("admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(bCryptPasswordEncoder().encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepo.save(admin);
                log.info("Admin created");
            }
        };
    }
}
