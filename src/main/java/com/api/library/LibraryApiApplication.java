package com.api.library;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.api.library.service.EmailService;

@SpringBootApplication
@EnableScheduling
public class LibraryApiApplication {
	
	@Autowired
	private EmailService emailService;

	
	//executa assim que subir a aplicação commandLineRunner
	@Bean
	public CommandLineRunner runner() {
		return args -> {List<String> emails = Arrays.asList("ddf70939e0-b722b9@inbox.mailtrap.io");
		emailService.sendEmails("testando serviço de emails", emails);
		};
	}
	
	@Bean
	public ModelMapper modelMaper() {
		return new ModelMapper();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
