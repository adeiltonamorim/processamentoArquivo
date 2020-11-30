package com.amorim.processador.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amorim.processador.domain.ItemVenda;
import com.amorim.processador.domain.Venda;
import com.amorim.processador.repository.ItemVendaRepository;
import com.amorim.processador.repository.VendaRepository;

@Service
public class VendaService {

	@Autowired
	private VendaRepository vendaRepository;
	
	@Autowired
	private ItemVendaRepository itemVendaRepository;
	
	public Venda save(Venda venda) {
		venda = vendaRepository.save(venda);
		
		for (ItemVenda itemVenda : venda.getItens()) {
			itemVendaRepository.save(itemVenda);
		}
		
		return venda;
	}
	
}
