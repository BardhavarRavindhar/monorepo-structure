package com.example.wallet.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getToken(String username) {
        return redisTemplate.opsForValue().get("token:" + username);
    }

    public boolean validateToken(String username, String token) {
        String storedToken = getToken(username);
        return token.equals(storedToken);
    }

}