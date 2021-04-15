package com.aulaspring.SB_projetocurso.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.aulaspring.SB_projetocurso.services.DBService;

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
}