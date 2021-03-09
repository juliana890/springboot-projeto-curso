package com.aulaspring.SB_projetocurso.config;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.aulaspring.SB_projetocurso.domain.Categoria;
import com.aulaspring.SB_projetocurso.domain.Cidade;
import com.aulaspring.SB_projetocurso.domain.Cliente;
import com.aulaspring.SB_projetocurso.domain.Endereco;
import com.aulaspring.SB_projetocurso.domain.Estado;
import com.aulaspring.SB_projetocurso.domain.Pagamento;
import com.aulaspring.SB_projetocurso.domain.PagamentoComBoleto;
import com.aulaspring.SB_projetocurso.domain.PagamentoComCartao;
import com.aulaspring.SB_projetocurso.domain.Pedido;
import com.aulaspring.SB_projetocurso.domain.Produto;
import com.aulaspring.SB_projetocurso.domain.enums.EstadoPagamento;
import com.aulaspring.SB_projetocurso.domain.enums.TipoCliente;
import com.aulaspring.SB_projetocurso.repositories.CategoriaRepository;
import com.aulaspring.SB_projetocurso.repositories.CidadeRepository;
import com.aulaspring.SB_projetocurso.repositories.ClienteRepository;
import com.aulaspring.SB_projetocurso.repositories.EnderecoRepository;
import com.aulaspring.SB_projetocurso.repositories.EstadoRepository;
import com.aulaspring.SB_projetocurso.repositories.PagamentoRepository;
import com.aulaspring.SB_projetocurso.repositories.PedidoRepository;
import com.aulaspring.SB_projetocurso.repositories.ProdutoRepository;

@Configuration
public class TesteConfig implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private EstadoRepository estadoRepository;

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired 
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Override
	public void run(String... args) throws Exception {

		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");

		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);

		// Associando as categorias aos produtos
		cat1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));

		// Associando os produtos as categorias
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));

		// Para salvar no banco de dados passamos uma lista de objetos
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1, p2, p3));

		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");

		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);

		// Associando os estados as cidades
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));

		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "mariasilva@gmail.com", "83862573492", TipoCliente.PESSOAFISICA);
		Cliente cli2 = new Cliente(null, "Emerson Sheik", "emersonsheik@gmail.com", "73621790425", TipoCliente.PESSOAJURIDICA);
		cli1.getTelefones().addAll(Arrays.asList("27363323", "93838393"));
		cli2.getTelefones().addAll(Arrays.asList("29473849"));
		
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 203", "Jardim", "38220834", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);
		Endereco e3 = new Endereco(null, "Rua Turquesa", "05", "Casa", "Parque", "03848820", cli2, c3);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));
		cli2.getEnderecos().addAll(Arrays.asList(e3));
		
		clienteRepository.saveAll(Arrays.asList(cli1, cli2));
		enderecoRepository.saveAll(Arrays.asList(e1, e2, e3));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));

	}

}
