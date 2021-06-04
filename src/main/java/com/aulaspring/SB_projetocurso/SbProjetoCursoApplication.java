package com.aulaspring.SB_projetocurso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.aulaspring.SB_projetocurso.services.S3Service;

@SpringBootApplication
public class SbProjetoCursoApplication implements CommandLineRunner {
	
	@Autowired
	private S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(SbProjetoCursoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		s3Service.uploadFile("C:\\Temp\\Imagens\\brigadeiro.jpg");
	}

}
