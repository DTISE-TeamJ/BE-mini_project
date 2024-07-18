package com.example.BE_mini_project;

import com.example.BE_mini_project.authentication.configuration.RsaKeyConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(RsaKeyConfigProperties.class)
public class BeMiniProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeMiniProjectApplication.class, args);
	}
}