package com.example.albaease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlbaEaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(AlbaEaseApplication.class, args);
    }
}
