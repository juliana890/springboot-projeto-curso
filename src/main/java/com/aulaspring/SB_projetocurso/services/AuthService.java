package com.aulaspring.SB_projetocurso.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aulaspring.SB_projetocurso.domain.Cliente;
import com.aulaspring.SB_projetocurso.repositories.ClienteRepository;
import com.aulaspring.SB_projetocurso.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	//Classe do Java que gera valores aleatórios
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));
		
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	//Método auxiliar que gera uma nova senha
	private String newPassword() {
		char[] vet = new char[10];
		
		for(int i = 0; i < 10; i++) {
			vet[i] = randomChar();
		}
		
		return new String(vet);
	}

	//Método que gera os caracteres
	private char randomChar() {
		
		//Gera números inteiros de 0 a 2
		int opt = rand.nextInt(3);
		
		if(opt == 0) { //Gera um digito
			return (char) (rand.nextInt(10) + 48); //Gerando um caracter correspondente ao digito de 0 a 9 | 48 é o número correspondente na tabela unicode para o 0
		}else if(opt == 1) { //Gera letra maiúscula
			return (char) (rand.nextInt(26) + 65); //Gerando um caracter correspondente ao alfabeto de A a Z | 65 é o número correspondente na tabela unicode para o A	
		}else { //Gera letra minúscula
			return (char) (rand.nextInt(26) + 97); //Gerando um caracter correspondente ao alfabeto de 'a' a 'z' | 97 é o número correspondente na tabela unicode para o 'a'
		}
	}

}
