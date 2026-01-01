package com.campuslostfound.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ContactController {

    @PostMapping
    public ResponseEntity<?> submitContact(@RequestBody Map<String, String> contactRequest) {
        try {
            // In a real implementation, you would:
            // 1. Save to database
            // 2. Send email notification
            // 3. Return success response
            
            // For now, just return success
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Contact form submitted successfully!"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to submit contact form: " + e.getMessage()
            ));
        }
    }
}
