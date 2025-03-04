package com.example.urlshortner.core.generator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;

@Component
public class Base62Generator implements IdGenerator{
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int length;
    private final SecureRandom random;

    public Base62Generator(@Value("${url.shortener.code.length:6}") int length) {
        this.length = length;
        this.random = new SecureRandom();
    }

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(randomIndex));
        }

        return sb.toString();
    }
}
