package com.example.urlshortner.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Primary
public class CacheAsideStorage implements StorageService {

    private final RedisTemplate<String, String> redisTemplate;
    private final StorageService mysqlStorage;

    private static final String CODE_PREFIX = "code:";
    private static final String URL_PREFIX = "url:";
    private static final long CACHE_TTL_HOURS = 24; // Cache TTL in hours

    @Autowired
    public CacheAsideStorage(
            RedisTemplate<String, String> redisTemplate,
            @Qualifier("mySQLDatabaseStorage") StorageService mysqlStorage) {
        this.redisTemplate = redisTemplate;
        this.mysqlStorage = mysqlStorage;
    }

    @Override
    @Transactional
    public void store(String shortCode, String longUrl) {
        // First store in the database
        mysqlStorage.store(shortCode, longUrl);

        // Then cache in Redis
        redisTemplate.opsForValue().set(CODE_PREFIX + shortCode, longUrl, CACHE_TTL_HOURS, TimeUnit.HOURS);
        redisTemplate.opsForValue().set(URL_PREFIX + longUrl, shortCode, CACHE_TTL_HOURS, TimeUnit.HOURS);
    }

    @Override
    public String getLongUrl(String shortCode) {
        // Try to get from cache first
        String cachedUrl = redisTemplate.opsForValue().get(CODE_PREFIX + shortCode);

        if (cachedUrl != null) {
            // Cache hit
            return cachedUrl;
        }

        // Cache miss - get from database
        String longUrl = mysqlStorage.getLongUrl(shortCode);

        // If found in database, cache it for future requests
        if (longUrl != null) {
            redisTemplate.opsForValue().set(CODE_PREFIX + shortCode, longUrl, CACHE_TTL_HOURS, TimeUnit.HOURS);
            redisTemplate.opsForValue().set(URL_PREFIX + longUrl, shortCode, CACHE_TTL_HOURS, TimeUnit.HOURS);
        }

        return longUrl;
    }

    @Override
    public String getShortCode(String longUrl) {
        // Try to get from cache first
        String cachedCode = redisTemplate.opsForValue().get(URL_PREFIX + longUrl);

        if (cachedCode != null) {
            // Cache hit
            return cachedCode;
        }

        // Cache miss - get from database
        String shortCode = mysqlStorage.getShortCode(longUrl);

        // If found in database, cache it for future requests
        if (shortCode != null) {
            redisTemplate.opsForValue().set(CODE_PREFIX + shortCode, longUrl, CACHE_TTL_HOURS, TimeUnit.HOURS);
            redisTemplate.opsForValue().set(URL_PREFIX + longUrl, shortCode, CACHE_TTL_HOURS, TimeUnit.HOURS);
        }

        return shortCode;
    }

    @Override
    @Transactional
    public boolean remove(String shortCode) {
        // Get the long URL before removal
        String longUrl = getLongUrl(shortCode);

        // Remove from database
        boolean result = mysqlStorage.remove(shortCode);

        // If successful and we have the long URL, also remove from cache
        if (result && longUrl != null) {
            redisTemplate.delete(CODE_PREFIX + shortCode);
            redisTemplate.delete(URL_PREFIX + longUrl);
        }

        return result;
    }

    @Override
    @Transactional
    public void clear() {
        // Clear the database
        mysqlStorage.clear();

        redisTemplate.delete(redisTemplate.keys(CODE_PREFIX + "*"));
        redisTemplate.delete(redisTemplate.keys(URL_PREFIX + "*"));
    }
}
