package com.aulaspring.SB_projetocurso.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.aulaspring.SB_projetocurso.security.UserSS;

public class UserService {
	
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch(Exception e) {
			return null;
		}
		
	}

}
