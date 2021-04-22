package com.aulaspring.SB_projetocurso.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.aulaspring.SB_projetocurso.services.DBService;
import com.aulaspring.SB_projetocurso.services.EmailService;
import com.aulaspring.SB_projetocurso.services.MockEmailService;

//#Para alterar a porta da aplicação manualmente server.port=${port:8081}

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		dbService.instantiateTestDatabase();
		
		return true;
	}
	
	//Criamos a instância de EmailService que é uma interface, retornando um MockEmailService que realiza o envio de email
	//Quando utilizamos a notação @Bean o método fica disponível como componente no sistema, ou seja o Spring busca a notation para realizar a instância
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}
