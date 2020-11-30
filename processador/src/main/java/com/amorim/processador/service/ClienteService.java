package com.amorim.processador.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amorim.processador.domain.Cliente;
import com.amorim.processador.repository.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente save(Long id, String cnpj, String nome, String businessArea) {
		Cliente cliente = new Cliente(id, cnpj, nome, businessArea);
		return clienteRepository.save(cliente);
	}

}
