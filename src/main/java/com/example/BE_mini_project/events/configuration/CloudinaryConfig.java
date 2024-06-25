package com.example.BE_mini_project.events.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dr1mrracx",
                "api_key", "428397366343216",
                "api_secret", "UUP0CFDLGnDd0r6SjrCMWpD8Osk"
        ));
    }
}