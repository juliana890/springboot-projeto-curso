package com.aulaspring.SB_projetocurso.services;

import org.springframework.mail.SimpleMailMessage;

import com.aulaspring.SB_projetocurso.domain.Pedido;

//Interface de serviço de email, operações que o serviço irá oferecer
public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);

	//Passamos o objeto de envio de email simples do SpringBoot
	void sendEmail(SimpleMailMessage msg);
}
