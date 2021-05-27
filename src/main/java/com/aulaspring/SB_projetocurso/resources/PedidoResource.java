package com.aulaspring.SB_projetocurso.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aulaspring.SB_projetocurso.domain.Pedido;
import com.aulaspring.SB_projetocurso.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoService service;
	
//	@GetMapping
//	public ResponseEntity<List<Pedido>> findAll() {
//		List<Pedido> lista = service.findAll();
//		
//		return ResponseEntity.ok().body(lista);
//	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Pedido> findById(@PathVariable Integer id){
		Pedido obj = service.findById(id);
		
		return ResponseEntity.ok().body(obj);
	}
	
	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj){
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping
	public ResponseEntity<Page<Pedido>> findPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, //Colocamos o defaultValue = "24" pq 24 é multiplo de 1, 2, 3 e 4 facilitando na implementação 
			@RequestParam(value = "orderBy", defaultValue = "instante") String orderBy, // Colocamos defaultValue = "nome" informando o campo que queremos ordenar
			@RequestParam(value = "direction", defaultValue = "DESC") String direction) {
		
		Page<Pedido> lista = service.findPage(page, linesPerPage, orderBy, direction);
		
		return ResponseEntity.ok().body(lista);
	}
	
}
