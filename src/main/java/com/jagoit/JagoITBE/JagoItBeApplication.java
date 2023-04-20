package com.jagoit.JagoITBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class JagoItBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JagoItBeApplication.class, args);
	}


}
