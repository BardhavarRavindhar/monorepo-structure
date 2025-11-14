package com.example.wallet.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final SecretKey jwtSecretKey;
    private final ObjectMapper objectMapper;

    public TokenService(
            RedisTemplate<String, String> redisTemplate,
            @Value("${jwt.secret:your-secret-key}") String secret) {
        this.redisTemplate = redisTemplate;
        this.jwtSecretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.objectMapper = new ObjectMapper();
    }

    public boolean validateToken(String token) {
        try {
            // First check if token exists in Redis
            String redisKey = "auth:token:" + token;
            String tokenData = redisTemplate.opsForValue().get(redisKey);

            if (tokenData == null) {
                return false; // Token not in Redis or expired
            }

            // Verify JWT signature (secondary validation)
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (JwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<TokenUserInfo> getUserInfoFromToken(String token) {
        try {
            String redisKey = "auth:token:" + token;
            String tokenData = redisTemplate.opsForValue().get(redisKey);

            if (tokenData == null) {
                return Optional.empty();
            }

            // Parse JSON data from Redis
            JsonNode jsonNode = objectMapper.readTree(tokenData);

            TokenUserInfo userInfo = new TokenUserInfo(
                    jsonNode.get("userId").asText(),
                    jsonNode.get("username").asText()
            );

            return Optional.of(userInfo);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Class to hold user info extracted from token
    public static class TokenUserInfo {
        private final String userId;
        private final String username;

        public TokenUserInfo(String userId, String username) {
            this.userId = userId;
            this.username = username;
        }

        public String getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }
    }
}