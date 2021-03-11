package com.aulaspring.SB_projetocurso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aulaspring.SB_projetocurso.domain.Pedido;
import com.aulaspring.SB_projetocurso.repositories.PedidoRepository;

import com.aulaspring.SB_projetocurso.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	public List<Pedido> findAll(){
		List<Pedido> lista = repo.findAll();
		
		return lista;
	}
	
	public Pedido findById(Integer id){
		Optional<Pedido> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ". Tipo: " + Pedido.class.getName()));
		
	}
	

}
