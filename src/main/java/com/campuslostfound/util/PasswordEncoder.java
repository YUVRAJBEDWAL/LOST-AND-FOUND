package com.campuslostfound.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        if (args.length > 0) {
            String password = args[0];
            String encoded = encoder.encode(password);
            System.out.println("Plain password: " + password);
            System.out.println("BCrypt encoded: " + encoded);
        } else {
            // Encode some default passwords
            String[] passwords = {"admin123", "password123", "password"};
            
            for (String password : passwords) {
                String encoded = encoder.encode(password);
                System.out.println(password + " -> " + encoded);
            }
        }
    }
}
