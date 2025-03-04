package com.example.urlshortner.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Service("mySQLDatabaseStorage")
@Profile("db")
public class DatabaseStorage implements StorageService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void store(String shortCode, String longUrl) {
        String sql = "INSERT INTO url_mappings (short_code, long_url, created_at) VALUES (?, ?, NOW())";
        jdbcTemplate.update(sql, shortCode, longUrl);
    }

    @Override
    public String getLongUrl(String shortCode) {
        String sql = "SELECT long_url FROM url_mappings WHERE short_code = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, shortCode);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getShortCode(String longUrl) {
        String sql = "SELECT short_code FROM url_mappings WHERE long_url = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, longUrl);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean remove(String shortCode) {
        String sql = "DELETE FROM url_mappings WHERE short_code = ?";
        return jdbcTemplate.update(sql, shortCode) > 0;
    }

    @Override
    @Transactional
    public void clear() {
        String sql = "DELETE FROM url_mappings";
        jdbcTemplate.update(sql);
    }
}
