package com.campuslostfound.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestDBController {

    private final JdbcTemplate jdbcTemplate;

    public TestDBController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/testdb")
    public Map<String, Object> testDatabaseConnection() {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            response.put("status", "SUCCESS");
            response.put("database", "MySQL");
            response.put("result", result);
        } catch (Exception e) {
            response.put("status", "FAILURE");
            response.put("error", e.getMessage());
        }
        return response;
    }
}
