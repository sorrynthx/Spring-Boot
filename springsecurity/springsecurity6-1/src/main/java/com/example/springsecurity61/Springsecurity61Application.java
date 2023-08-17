package com.example.springsecurity61;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class Springsecurity61Application {

	public static void main(String[] args) {
		SpringApplication.run(Springsecurity61Application.class, args);
	}

}
