package com.campuslostfound.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
            "message", "Welcome to Campus Lost & Found Backend API",
            "status", "RUNNING",
            "endpoints", Map.of(
                "health", "/testdb",
                "auth", "/api/auth",
                "items", "/api/items",
                "claims", "/api/claims",
                "admin", "/api/admin"
            )
        );
    }
}
