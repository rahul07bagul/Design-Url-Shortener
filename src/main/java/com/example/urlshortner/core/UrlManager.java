package com.example.urlshortner.core;

import com.example.urlshortner.storage.StorageService;
import com.example.urlshortner.core.generator.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlManager {
    private final StorageService storageService;
    private final IdGenerator idGenerator;

    @Autowired
    public UrlManager(StorageService storageService, IdGenerator idGenerator) {
        this.storageService = storageService;
        this.idGenerator = idGenerator;
    }

    public String shortenUrl(String url) {
        if(url == null){
            throw new IllegalArgumentException("Url cannot be blank");
        }

        String existingCode = storageService.getShortCode(url);
        if(existingCode != null){
            return existingCode;
        }

        String shortCode;
        do{
            shortCode = idGenerator.generate();
        }while(storageService.getLongUrl(shortCode) != null);


        storageService.store(shortCode, url);
        return shortCode;
    }

    public String expandUrl(String shortCode) {
        if(shortCode.isEmpty()){
            throw new IllegalArgumentException("Url cannot be blank");
        }

        String longUrl = storageService.getLongUrl(shortCode);
        if (longUrl == null) {
            throw new IllegalArgumentException("Short URL not found: " + shortCode);
        }

        return longUrl;
    }
}
