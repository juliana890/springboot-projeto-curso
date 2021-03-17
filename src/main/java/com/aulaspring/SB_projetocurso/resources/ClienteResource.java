package com.aulaspring.SB_projetocurso.resources;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aulaspring.SB_projetocurso.domain.Cliente;
import com.aulaspring.SB_projetocurso.dto.ClienteDTO;
import com.aulaspring.SB_projetocurso.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;
	
	@GetMapping
	public ResponseEntity<List<Cliente>> findAll(){
		List<Cliente> lista = service.findAll();
		
		return ResponseEntity.ok().body(lista);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Cliente> findById(@PathVariable Integer id){
		Cliente obj = service.findById(id);
		
		return ResponseEntity.ok().body(obj);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDto, @PathVariable Integer id){
		Cliente obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		
		return ResponseEntity.noContent().build();
		
	}
	
	//http://localhost:8080/clientes/page?linesPerPage=3&page=1&direction=DESC
	@GetMapping(value = "/page")
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, //Colocamos o defaultValue = "24" pq 24 é multiplo de 1, 2, 3 e 4 facilitando na implementação 
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy, // Colocamos defaultValue = "nome" informando o campo que queremos ordenar
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		Page<Cliente> lista = service.findPage(page, linesPerPage, orderBy, direction);
		Page<ClienteDTO> listDto = lista.map(obj -> new ClienteDTO(obj));
		
		return ResponseEntity.ok().body(listDto);
	}
	
//	@PostMapping
//	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteDTO objDto){
//		Cliente obj = service.fromDTO(objDto);
//		obj = service.insert(obj);
//		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
//		
//		return ResponseEntity.created(uri).build();
//	}

}
