package com.aulaspring.SB_projetocurso.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

//Classe de autorização com consulta via token no headers
public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

	private JWTUtil jwtUtil;
	
	private UserDetailsService userDetailsService;
	
	//Passamos o usuário tbm como parâmetro para verificar se ele está autorizado
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;		
	}
	
	//É necessário pegar o token que veio na requisição
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		//Buscamos o header da requisição, com isso pegamos o valor do parâmetro Authorization
		String header = request.getHeader("Authorization");
		
		//Verificamos se o cabeçalho começa com a palavra Bearer
		if(header != null && header.startsWith("Bearer ")) {
			//Passamos o valor do token para o objeto UsernamePasswordAuthenticationToken que é do Spring Security
			//E pegamos o valor do token com o substring
			UsernamePasswordAuthenticationToken auth = getAuthentication(request, header.substring(7));
			
			if(auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		//Para continuar com a requisição
		chain.doFilter(request, response);
	}

	//Método que irá gerar um objeto UsernamePasswordAuthenticationToken a partir do token que veio como parâmetro da requisição
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String token) {
		if(jwtUtil.tokenValido(token)) {
			//Pegando o username dentro do token
			String username = jwtUtil.getUsername(token);
			
			//A partir do username instânciamos um UserDetails
			UserDetails user = userDetailsService.loadUserByUsername(username);
			
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		
		return null;
	}

}
