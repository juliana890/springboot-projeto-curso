package com.aulaspring.SB_projetocurso.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aulaspring.SB_projetocurso.dto.CredenciaisDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

//Classe responsável por executar o filtro de autenticação, extendemos da classe responsável pela autenticação
//Quando uma classe extends de UsernamePasswordAuthenticationFilter o Spring sabe que terá que interceptar o login
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authenticationManager;
	
	private JWTUtil jwtUtil;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}
	
	//Método que irá tentar a autenticação
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req,HttpServletResponse res) throws AuthenticationException {
		//Buscamos o email e senha na requisição que são digitados anteriormente pelo usuário
		//E converte para o tipo CredenciaisDTO
		try {
			//Instânciamos um objeto do tipo CredenciaisDTO com os dados que vieram da requisição
			CredenciaisDTO creds = new ObjectMapper().readValue(req.getInputStream(), CredenciaisDTO.class);
			
			//Obtendo os dados que são email e senha, instânciamos um objeto do tipo UsernamePasswordAuthenticationToken
			//Esse token é do Spring Security
			//Precisamos passar o usuário, senha e uma lista vazia
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());
			
			//.authenticate é que verifica se os dados do usuário são válidos
			//O framework verifica com base nas implementações do UserDetailsService
			Authentication auth = authenticationManager.authenticate(authToken);
			
			return auth;
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	//Se a autenticação for realizada com sucesso
	//O método pega o auth que foi gerado no método acima
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
		
		//auth.getPrincipal() retorna o usuário do Spring Security e obtemos o email através do .getUsername()
		String username = ((UserSS) auth.getPrincipal()).getUsername();
		//A partir do email geramos o token passando esse email
		String token = jwtUtil.generateToken(username);
		//O token retorna e acrescentamos ele no cabeçalho da resposta
		res.addHeader("Authorization", "Bearer " + token);
		//Expondo o cabeçalho para outras aplicações
		res.addHeader("access-control-expose-headers", "Authorization");
	}
	
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {
		 
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
                throws IOException, ServletException {
            response.setStatus(401);
            response.setContentType("application/json"); 
            response.getWriter().append(json());
        }
        
        private String json() {
            long date = new Date().getTime();
            return "{\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\": \"Não autorizado\", "
                + "\"message\": \"Email ou senha inválidos\", "
                + "\"path\": \"/login\"}";
        }
    }

}
