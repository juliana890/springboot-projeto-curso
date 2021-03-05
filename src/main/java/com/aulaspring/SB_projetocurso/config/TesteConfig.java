package com.aulaspring.SB_projetocurso.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.aulaspring.SB_projetocurso.domain.Categoria;
import com.aulaspring.SB_projetocurso.repositories.CategoriaRepository;

@Configuration
public class TesteConfig implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Eletrônicos");
		Categoria cat3 = new Categoria(null, "Jardinagem");
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3)); //Para salvar no banco de dados passamos uma lista de objetos
		
	}

}
