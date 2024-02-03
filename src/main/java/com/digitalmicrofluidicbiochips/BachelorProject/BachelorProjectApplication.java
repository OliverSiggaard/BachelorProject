package com.digitalmicrofluidicbiochips.BachelorProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BachelorProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BachelorProjectApplication.class, args);
	}

	@CrossOrigin(origins = "http://localhost:3000") // Allows requests from localhost:3000
	@GetMapping("/root")
	public String apiRoot() {
		return "Hello from Spring Boot!";
	}
}