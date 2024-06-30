package com.example.BE_mini_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BeMiniProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeMiniProjectApplication.class, args);
	}
}
