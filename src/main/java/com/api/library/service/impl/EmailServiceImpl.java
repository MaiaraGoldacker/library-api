package com.api.library.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.api.library.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	@Value("${application.mail.defaultremetent}")
	private String remetent;
	
	private final JavaMailSender javaMailSender;
	
	@Override
	public void sendEmails(String message, List<String> mailsList) {
		// TODO Auto-generated method stub
		//ddf70939e0-0f9e2f@inbox.mailtrap.io
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(remetent); //remetente
		mailMessage.setSubject("Livro com empréstimo atrasado"); //assunto
		mailMessage.setText(message); //mensagem
		mailMessage.setTo(mailsList.toArray(new String[mailsList.size()])); //tranformar list em array
	
		javaMailSender.send(mailMessage);
	}

}
