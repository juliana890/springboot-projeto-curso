package com.aulaspring.SB_projetocurso.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private Environment env;

	//Liberamos somente o h2
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"
	};
	
	//Liberamos os caminhos somente para leitura
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**"
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
		http.authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll().antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll().anyRequest().authenticated();
		
		//Para assegurar que o back-end não irá criar seção de usuário
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	//Definindo acesso básico de multiplas fontes para todos os caminhos
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		
		return source;
	}
}
