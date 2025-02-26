package com.example.albaease;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void encodePassword() {
        String[] passwords = {
                "albaease123",

        };

        for (String password : passwords) {
            String encodedPassword = passwordEncoder.encode(password);
            System.out.println(password + " -> " + encodedPassword);
        }
    }
}
