package com.aulaspring.SB_projetocurso.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.aulaspring.SB_projetocurso.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		
		SimpleMailMessage sm = SimpleMailMessageFromPedido(obj);
		sendEmail(sm);
	}

	//Método auxiliar para instânciar um SimpleMailMessage
	protected SimpleMailMessage SimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		
		sm.setTo(obj.getCliente().getEmail()); //Destinatário: enviando para o cliente que realizou o pedido
		sm.setFrom(sender); //Email de envio
		sm.setSubject("Pedido Confirmado! Código: " + obj.getId()); //Assunto do email
		sm.setSentDate(new Date(System.currentTimeMillis()));//Data do email, colocamos System.currentTimeMillis para garantir que a data do servidor
		sm.setText(obj.toString()); //Corpo do email, com detalhes do Pedido
		
		return sm;
	}

}
