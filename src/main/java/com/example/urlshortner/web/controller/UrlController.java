package com.example.urlshortner.web.controller;

import com.example.urlshortner.core.UrlManager;
import com.example.urlshortner.web.dto.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:5173")
public class UrlController {

    private final UrlManager urlManager;
    private final String baseUrl;

    @Autowired
    public UrlController(UrlManager urlManager, @Value("${url.shortener.base.url}") String baseUrl) {
        this.urlManager = urlManager;
        this.baseUrl = baseUrl;
    }

    @PostMapping("/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody UrlRequest request) {
        String shortCode = urlManager.shortenUrl(request.getLongUrl());

        UrlResponse response = new UrlResponse();
        response.setOriginalUrl(request.getLongUrl());
        response.setShortCode(shortCode);
        response.setShortUrl(baseUrl + "/" + shortCode);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/info/{shortCode}")
    public ResponseEntity<UrlResponse> getUrlInfo(@PathVariable String shortCode) {
        String longUrl = urlManager.expandUrl(shortCode);

        UrlResponse response = new UrlResponse();
        response.setOriginalUrl(longUrl);
        response.setShortCode(shortCode);
        response.setShortUrl(baseUrl + "/" + shortCode);

        return ResponseEntity.ok(response);
    }
}