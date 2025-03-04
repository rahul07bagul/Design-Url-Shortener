package com.example.urlshortner.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("redis")
public class RedisStorage implements StorageService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String CODE_PREFIX = "code:";
    private static final String URL_PREFIX = "url:";

    @Autowired
    public RedisStorage(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void store(String shortCode, String longUrl) {
        redisTemplate.opsForValue().set(CODE_PREFIX + shortCode, longUrl);
        redisTemplate.opsForValue().set(URL_PREFIX + longUrl, shortCode);
    }

    @Override
    public String getLongUrl(String shortCode) {
        return redisTemplate.opsForValue().get(CODE_PREFIX + shortCode);
    }

    @Override
    public String getShortCode(String longUrl) {
        return redisTemplate.opsForValue().get(URL_PREFIX + longUrl);
    }

    @Override
    public boolean remove(String shortCode) {
        String longUrl = getLongUrl(shortCode);
        if (longUrl != null) {
            redisTemplate.delete(CODE_PREFIX + shortCode);
            redisTemplate.delete(URL_PREFIX + longUrl);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}