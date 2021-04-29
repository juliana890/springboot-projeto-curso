package com.aulaspring.SB_projetocurso.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	//Método que irá gerar o token
	public String generateToken(String username) {
		//Para gerar o token chamamos o método do JWT
		//setSubject passamos o usuário
		//setExpiration passamos um Date com base no tempo de expiração, horário atual mais o tempo de expiração
		//signWith como iremos assinar o token, escolhemos o algoritmo HS512 e passamos nosso atributo secret.getBytes pq o método busca um array de bytes
		//Para finalizar chamamos o .compact();
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
}
