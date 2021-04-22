package com.aulaspring.SB_projetocurso.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.aulaspring.SB_projetocurso.services.DBService;
import com.aulaspring.SB_projetocurso.services.EmailService;
import com.aulaspring.SB_projetocurso.services.SmtpEmailService;

//#Para alterar a porta da aplicação manualmente server.port=${port:8081}

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBService dbService;
	
	//Dessa forma pegamos o valor da chave
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		//Verificando qual o valor do parâmetro spring.jpa.hibernate.ddl-auto do arquivo application-dev
		if(!"create".equals(strategy)) {
			return false;
		}
		
		dbService.instantiateTestDatabase();
		
		return true;
	}
	
	//Criamos a instância de EmailService que é uma interface, retornando um MockEmailService que realiza o envio de email de verdade
	//Quando utilizamos a notação @Bean o método fica disponível como componente no sistema, ou seja o Spring busca a notation para realizar a instância
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}
