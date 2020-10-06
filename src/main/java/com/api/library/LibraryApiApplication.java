package com.api.library;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/*http://localhost:8080/swagger-ui.html#/book-controller*/

@SpringBootApplication
@EnableScheduling
public class LibraryApiApplication {
	
	@Bean
	public ModelMapper modelMaper() {
		return new ModelMapper();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
