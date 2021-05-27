package com.aulaspring.SB_projetocurso.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aulaspring.SB_projetocurso.domain.enums.Perfil;

//Classe de contrato implementa a classe UserDetails do Spring Security
public class UserSS implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String email;
	private String senha;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserSS() {}
	
	//O construtor irá receber uma lista de Perfis e não uma Collection
	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		//Aqui passamos os Perfis para Collection com expressão lambda
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
	}



	public Integer getId() {
		return id;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	//Testamos o papel do admin
	public boolean hasRole(Perfil perfil) {
		//Precisamos acessar a lista de authorities
		//Depois precisamos converter o Perfil para GrantedAuthority
		//Passamos o perfil em forma de String
		return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
	}
}
