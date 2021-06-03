package com.aulaspring.SB_projetocurso.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.aulaspring.SB_projetocurso.domain.Cliente;
import com.aulaspring.SB_projetocurso.domain.Pedido;

//Interface de serviço de email, operações que o serviço irá oferecer
public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);

	//Passamos o objeto SimpleMailMessage de envio de email simples do SpringBoot
	void sendEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHtmlEmail(Pedido obj);
	
	//Passamos o objeto MimeMessage para realizar o envio de email com o template HTML
	void sendHtmlEmail(MimeMessage msg);
	
	void sendNewPasswordEmail(Cliente cliente, String newPass);
}
