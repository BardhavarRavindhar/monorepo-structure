package com.example.wallet.security;

import com.example.wallet.model.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        // Skip authentication for non-protected URLs
        if (!request.getRequestURI().startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        // Check if header is missing or malformed
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // Let the AuthenticationEntryPoint handle this
            chain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.substring(7);

        try {
            // Validate JWT signature
            if (jwtUtil.validateToken(jwt)) {
                String username = jwtUtil.extractUsername(jwt);
                String userId = jwtUtil.extractUserId(jwt);

                // Validate in Redis
                if (username != null && redisService.validateToken(username, jwt)) {
                    // Create custom user details
                    CustomUserDetails userDetails = new CustomUserDetails(username, userId);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    chain.doFilter(request, response);
                    return;
                }
            }

            // If we get here, authentication failed but let's continue the chain
            // to let the AuthenticationEntryPoint handle it
            chain.doFilter(request, response);

        } catch (Exception e) {
            // For any JWT parsing/validation exceptions, let the entry point handle it
            chain.doFilter(request, response);
        }
    }
}