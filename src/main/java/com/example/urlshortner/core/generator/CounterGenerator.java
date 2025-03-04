package com.example.urlshortner.core.generator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

public class CounterGenerator implements IdGenerator {

    private final AtomicLong counter;
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALPHABET.length();

    public CounterGenerator(@Value("${url.shortener.counter.start:0}") long startValue) {
        this.counter = new AtomicLong(startValue);
    }

    @Override
    public String generate() {
        return encodeBase62(counter.getAndIncrement());
    }


    private String encodeBase62(long value) {
        StringBuilder sb = new StringBuilder();

        do {
            sb.insert(0, ALPHABET.charAt((int) (value % BASE)));
            value = value / BASE;
        } while (value > 0);

        return sb.toString();
    }
}
