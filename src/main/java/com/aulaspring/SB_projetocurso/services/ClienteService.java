package com.aulaspring.SB_projetocurso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aulaspring.SB_projetocurso.domain.Cidade;
import com.aulaspring.SB_projetocurso.domain.Cliente;
import com.aulaspring.SB_projetocurso.domain.Endereco;
import com.aulaspring.SB_projetocurso.domain.enums.Perfil;
import com.aulaspring.SB_projetocurso.domain.enums.TipoCliente;
import com.aulaspring.SB_projetocurso.dto.ClienteDTO;
import com.aulaspring.SB_projetocurso.dto.ClienteNewDTO;
import com.aulaspring.SB_projetocurso.repositories.ClienteRepository;
import com.aulaspring.SB_projetocurso.repositories.EnderecoRepository;
import com.aulaspring.SB_projetocurso.security.UserSS;
import com.aulaspring.SB_projetocurso.services.exceptions.AuthorizationException;
import com.aulaspring.SB_projetocurso.services.exceptions.DataIntegrityException;
import com.aulaspring.SB_projetocurso.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	//Criamos o BCryptPasswordEncoder para passar a senha com a cryptografia 
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public List<Cliente> findAll(){
		List<Cliente> lista = repo.findAll();
		
		return lista;
	}
	
	public Cliente findById(Integer id) {
		
		//Pegando o usuário logado
		UserSS user = UserService.authenticated();
		
		//Verificamos se o usuário não está nulo e se ele não possui papel ADMIN e se o id é diferente do usuário logado
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ". Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = findById(obj.getId());
		updateData(obj, newObj);
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		findById(id);
		
		try {
			repo.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados!");
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {		
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		
		if(objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		if(objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;
	}
	
	private void updateData(Cliente obj, Cliente newObj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

}
