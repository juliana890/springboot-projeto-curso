package com.aulaspring.SB_projetocurso.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aulaspring.SB_projetocurso.domain.Produto;
import com.aulaspring.SB_projetocurso.dto.ProdutoDTO;
import com.aulaspring.SB_projetocurso.resources.utils.URL;
import com.aulaspring.SB_projetocurso.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service;
	
//	@GetMapping
//	public ResponseEntity<List<Produto>> findAll() {
//		List<Produto> lista = service.findAll();
//		
//		return ResponseEntity.ok().body(lista);
//	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Produto> findById(@PathVariable Integer id){
		Produto obj = service.findById(id);
		
		return ResponseEntity.ok().body(obj);
	}
	
	//http://localhost:8080/produtos/?nome=computador&categorias=1,2,3
	@GetMapping
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value = "nome", defaultValue = "") String nome, 
			@RequestParam(value = "categorias", defaultValue = "") String categorias, 
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, //Colocamos o defaultValue = "24" pq 24 é multiplo de 1, 2, 3 e 4 facilitando na implementação 
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy, // Colocamos defaultValue = "nome" informando o campo que queremos ordenar
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		String nomeDecoded = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(categorias);
		Page<Produto> lista = service.search(nomeDecoded, ids, page, linesPerPage, orderBy, direction);
		Page<ProdutoDTO> listDto = lista.map(obj -> new ProdutoDTO(obj));
		
		return ResponseEntity.ok().body(listDto);
	}
	
}
