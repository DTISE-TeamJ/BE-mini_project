package com.example.BE_mini_project.authentication.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BlacklistAuthRedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public BlacklistAuthRedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void blacklistToken(String token) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "blacklisted");
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }
}
