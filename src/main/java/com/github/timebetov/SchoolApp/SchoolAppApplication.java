package com.github.timebetov.SchoolApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.github.timebetov.SchoolApp.repository")
@EntityScan("com.github.timebetov.SchoolApp.model")
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class SchoolAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolAppApplication.class, args);
	}

}