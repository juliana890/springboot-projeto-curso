package com.aulaspring.SB_projetocurso.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.aulaspring.SB_projetocurso.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		//Definindo a data de pagamento 7 dias após a data do Pedido
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido); //Instânciando a data a partir do instante do Pedido
		cal.add(Calendar.DAY_OF_MONTH, 7); //Acrescentando 7 dias 
		pagto.setDataVencimento(cal.getTime());
	}

}
