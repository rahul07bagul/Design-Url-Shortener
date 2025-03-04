package com.example.urlshortner.storage;

public interface StorageService {
    void store(String shortCode, String longUrl);
    String getLongUrl(String shortCode);
    String getShortCode(String longUrl);
    boolean remove(String shortCode);
    void clear();
}
