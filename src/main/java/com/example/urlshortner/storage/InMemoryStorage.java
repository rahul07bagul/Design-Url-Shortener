package com.example.urlshortner.storage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Profile("dev")
public class InMemoryStorage implements StorageService {

    // Map for short code to long URL
    private final Map<String, String> codeToUrl = new HashMap<>();

    // Map for long URL to short code (reverse mapping)
    private final Map<String, String> urlToCode = new HashMap<>();

    @Override
    public void store(String shortCode, String longUrl) {
        codeToUrl.put(shortCode, longUrl);
        urlToCode.put(longUrl, shortCode);
    }

    @Override
    public String getLongUrl(String shortCode) {
        return codeToUrl.get(shortCode);
    }

    @Override
    public String getShortCode(String longUrl) {
        return urlToCode.get(longUrl);
    }

    @Override
    public boolean remove(String shortCode) {
        String longUrl = codeToUrl.remove(shortCode);
        if (longUrl != null) {
            urlToCode.remove(longUrl);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        codeToUrl.clear();
        urlToCode.clear();
    }
}