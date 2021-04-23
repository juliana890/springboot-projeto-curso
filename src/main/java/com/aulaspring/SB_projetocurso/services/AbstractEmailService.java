package com.aulaspring.SB_projetocurso.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.aulaspring.SB_projetocurso.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${default.sender}")
	private String sender;
	
	//Criamos a instância para o template
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
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
		sm.setSubject("Pedido confirmado! Número do pedido: " + obj.getId()); //Assunto do email
		sm.setSentDate(new Date(System.currentTimeMillis()));//Data do email, colocamos System.currentTimeMillis para garantir que a data do servidor
		sm.setText(obj.toString()); //Corpo do email, com detalhes do Pedido
		
		return sm;
	}
	
	//Método que irá pegar o nosso template e popular com os dados e retornar uma String
	protected String htmlFromTemplatePedido(Pedido obj) {
		Context context = new Context();
		
		//A partir do objeto context, pegamos o Pedido e enviamos para o nosso template
		//'pedido' é o mesmo nome que corresponde com o nosso template
		//O comando abaixo define que o obj será passado com o apelido pedido
		context.setVariable("pedido", obj);
		
		//Passamos o caminho do template
		return templateEngine.process("email/confirmacaoPedido", context);
	}
	
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {
		try {
			MimeMessage mm = prepareMimeMessageFromPedido(obj);
			sendHtmlEmail(mm);
		}
		catch(MessagingException e) {
			//Caso houver erro no MimeMessage enviamos um email simples
			sendOrderConfirmationEmail(obj);
		}
		
	}

	//Método responsável por prepapar o email com MimeMessage a partir do obj Pedido
	protected MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		//Para atribuirmos valores a mensagem
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(obj.getCliente().getEmail());//Enviando para o email do cliente
		mmh.setFrom(sender);//Email remetente
		mmh.setSubject("Pedido confirmado! Número do pedido: " + obj.getId());//Assunto do email
		mmh.setSentDate(new Date(System.currentTimeMillis()));//Istante do email
		mmh.setText(htmlFromTemplatePedido(obj), true);//Corpo do email será nosso template passamos o htmlFromTemplatePedido com o obj como parâmetro e passamos o true para indicar que é html
		
		return mimeMessage;
	}
}
