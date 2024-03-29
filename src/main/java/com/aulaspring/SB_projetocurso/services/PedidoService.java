package com.aulaspring.SB_projetocurso.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aulaspring.SB_projetocurso.domain.Cliente;
import com.aulaspring.SB_projetocurso.domain.ItemPedido;
import com.aulaspring.SB_projetocurso.domain.PagamentoComBoleto;
import com.aulaspring.SB_projetocurso.domain.Pedido;
import com.aulaspring.SB_projetocurso.domain.enums.EstadoPagamento;
import com.aulaspring.SB_projetocurso.repositories.ItemPedidoRepository;
import com.aulaspring.SB_projetocurso.repositories.PagamentoRepository;
import com.aulaspring.SB_projetocurso.repositories.PedidoRepository;
import com.aulaspring.SB_projetocurso.security.UserSS;
import com.aulaspring.SB_projetocurso.services.exceptions.AuthorizationException;
import com.aulaspring.SB_projetocurso.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	//Criamos uma instância do EmailService para realizar o "envio" do email
	@Autowired
	private EmailService emailService;
	
	public List<Pedido> findAll(){
		List<Pedido> lista = repo.findAll();
		
		return lista;
	}
	
	public Pedido findById(Integer id){
		Optional<Pedido> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ". Tipo: " + Pedido.class.getName()));
		
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.findById(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			
			//Preencher o boleto com o vencimento após 7 dias do instante do Pedido e o pagamento
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for(ItemPedido ip : obj.getItems()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.findById(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
		//Salvamos os itens no banco de dados
		itemPedidoRepository.saveAll(obj.getItems());
		
		//Enviando o email simples
		//emailService.sendOrderConfirmationEmail(obj);
		
		//Enviando o email em HTML conforme template
		emailService.sendOrderConfirmationHtmlEmail(obj);
		
		return obj;
		
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		UserSS user = UserService.authenticated();
		
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		//Para retornar os pedidos do cliente logado
		Cliente cliente = clienteService.findById(user.getId());
		
		return repo.findByCliente(cliente, pageRequest);
	}
}
