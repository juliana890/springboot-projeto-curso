package com.aulaspring.SB_projetocurso.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService {
	
	//Instânciamos para mostrar o envio falso do email no log do servidor
	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Simulando o envio de email...");
		LOG.info(msg.toString());
		LOG.info("Email enviado!");
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Simulando o envio de email HTML...");
		LOG.info(msg.toString());
		LOG.info("Email enviado!");
		
	}

}
