package com.aulaspring.SB_projetocurso.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailService extends AbstractEmailService {

	//Automáticamente o framework cria a instância do MailSender com todos os dados que foram passados no application-dev
	@Autowired
	private MailSender mailSender;
	
	//Para enviarmos o email no formato HTML do template criamos a instância do JavaMailSender
	@Autowired
	private JavaMailSender javaMailSender;
	
	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando email...");
		mailSender.send(msg); //Para enviar o email
		LOG.info("Email enviado!");
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Enviando email...");
		javaMailSender.send(msg); //Para enviar o email utilizamos o JavaMailSender que é capaz de ler um MimeMessage
		LOG.info("Email enviado!");
		
	}

	
	
}
