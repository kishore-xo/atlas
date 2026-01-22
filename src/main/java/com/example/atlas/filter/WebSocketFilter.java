package com.example.atlas.filter;

import com.example.atlas.util.CustomUserDetailService;
import com.example.atlas.util.JwtUtil;
import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class WebSocketFilter implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailService userDetailService;

    public WebSocketFilter(JwtUtil jwtUtil, CustomUserDetailService userDetailService) {
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
    }

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            try {
                String authHeader = accessor.getFirstNativeHeader("Authorization");
                if (authHeader == null) {
                    authHeader = accessor.getFirstNativeHeader("authorization");
                }

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new AccessDeniedException("Missing authentication token");
                }

                String token = authHeader.substring(7);

                if (jwtUtil.isTokenExpired(token)) {
                    throw new AccessDeniedException("Token expired");
                }

                String email = jwtUtil.extractEmail(token);
                UserDetails userDetails = userDetailService.loadUserByUsername(email);

                Authentication authentication = new
                        UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                accessor.setUser(authentication);
            } catch (Exception e) {
                System.out.println("WebSocket Auth Failed: " + e.getMessage());
                e.printStackTrace();
                throw new AccessDeniedException("Authentication failed: " + e.getMessage());
            }
        }
        return message;
    }

}
