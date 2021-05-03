package com.aulaspring.SB_projetocurso.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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
	
	//Testando se um token é válido
	public boolean tokenValido(String token) {
		//Claims é um tipo no JWT que armazena as reinvidicações do token
		Claims claims = getClaims(token);
		
		if(claims != null) {
			//Pegamos o usuário
			String username = claims.getSubject();
			
			//Pegamos a data de expiraração do token e a data atual para testarmos se o token já está expirado
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());
			
			//Se token for válido
			if(username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		
		return false;
	}
	
	public String getUsername(String token) {
		Claims claims = getClaims(token);
		
		if(claims != null) {
			return claims.getSubject();
		}
		
		return null;
	}
	
	//Para obter os Claims a partir do token
	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		}
		catch(Exception e) {
			return null;
		}
		
	}
}
