package com.example.atlas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
@EnableCaching
@EnableScheduling
public class AtlasApplication {

    static void main(String[] args) {
        SpringApplication.run(AtlasApplication.class, args);
    }

}
