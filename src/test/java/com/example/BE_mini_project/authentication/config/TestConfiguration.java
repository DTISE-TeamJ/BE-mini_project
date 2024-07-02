package com.example.BE_mini_project.authentication.config;

import com.example.BE_mini_project.authentication.services.AuthenticationServiceTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfiguration {

    @Bean
    public AuthenticationServiceTest authenticationServiceTest() {
        return new AuthenticationServiceTest();
    }
}
