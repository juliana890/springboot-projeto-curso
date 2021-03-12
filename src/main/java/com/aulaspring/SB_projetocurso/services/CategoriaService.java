package com.aulaspring.SB_projetocurso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aulaspring.SB_projetocurso.domain.Categoria;
import com.aulaspring.SB_projetocurso.repositories.CategoriaRepository;

import com.aulaspring.SB_projetocurso.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public List<Categoria> findAll(){
		List<Categoria> lista = repo.findAll();
		
		return lista;
	}
	
	public Categoria findById(Integer id){
		Optional<Categoria> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ". Tipo: " + Categoria.class.getName()));
		
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	

}
