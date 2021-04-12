package com.aulaspring.SB_projetocurso.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aulaspring.SB_projetocurso.domain.ItemPedido;
import com.aulaspring.SB_projetocurso.domain.PagamentoComBoleto;
import com.aulaspring.SB_projetocurso.domain.Pedido;
import com.aulaspring.SB_projetocurso.domain.enums.EstadoPagamento;
import com.aulaspring.SB_projetocurso.repositories.ItemPedidoRepository;
import com.aulaspring.SB_projetocurso.repositories.PagamentoRepository;
import com.aulaspring.SB_projetocurso.repositories.PedidoRepository;
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
			ip.setPreco(produtoService.findById(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);
		}
		
		//Salvamos os itens no banco de dados
		itemPedidoRepository.saveAll(obj.getItems());
		
		return obj;
		
	}
	

}
