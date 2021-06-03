package com.aulaspring.SB_projetocurso.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.aulaspring.SB_projetocurso.security.JWTAuthenticationFilter;
import com.aulaspring.SB_projetocurso.security.JWTAuthorizationFilter;
import com.aulaspring.SB_projetocurso.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	//Criamos a instância da interface pq o Spring busca quem está implementando para criar a instância da impl
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JWTUtil jwtUtil;

	//Liberamos somente o h2
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"
	};
	
	//Liberamos os caminhos somente para leitura
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**"
	};
	
	//Liberamos os caminhos somente para cadastro
		private static final String[] PUBLIC_MATCHERS_POST = {
				"/clientes/**",
				"/auth/forgot/**"
		};
	
	//Sobrescrevendo o método do WebSecurityConfigurerAdapter
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//Pegamos os profiles ativos, se for test liberamos o acesso ao banco h2
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		//Acessando o obj http, passamos o nosso vetor como parâmetro e permitimos os acessos
		//E para todo o resto exige a autenticação
		http.cors().and().csrf().disable(); //Para o @Bean ser ativado, e para desabilitarmos a seção
		//antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET permitimos apenas o método GET
		http.authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll().antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll().antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll().anyRequest().authenticated();
		
		//Adicionamos o filtro criado na classe JWTAuthenticationFilter
		//Passamos o authenticationManager() que já pertence a classe WebSecurityConfigurerAdapter e o nosso JWTutil
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		
		//Adicionamos o filtro de autorização criado na classe JWTAuthorizationFilter
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		
		//Para assegurar que o back-end não irá criar seção de usuário
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	//Definindo acesso básico de multiplas fontes para todos os caminhos
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		
		return source;
	}
	
	//Método encode para criptografar a senha
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
