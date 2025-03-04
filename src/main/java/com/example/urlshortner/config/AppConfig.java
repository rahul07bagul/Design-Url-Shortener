package com.example.urlshortner.config;

import com.example.urlshortner.core.generator.Base62Generator;
import com.example.urlshortner.core.generator.CounterGenerator;
import com.example.urlshortner.core.generator.IdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Main application configuration
 */
@Configuration
public class AppConfig {

    @Value("${url.shortener.generator.type:base62}")
    private String generatorType;

    @Value("${url.shortener.code.length:6}")
    private int codeLength;

    @Value("${url.shortener.counter.start:0}")
    private long counterStart;


    @Bean
    @Primary
    public IdGenerator idGenerator() {
        if ("counter".equalsIgnoreCase(generatorType)) {
            return new CounterGenerator(counterStart);
        } else {
            return new Base62Generator(codeLength);
        }
    }
}
