package com.amorim.processador.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amorim.processador.domain.Vendedor;
import com.amorim.processador.repository.VendedorRepository;

@Service
public class VendedorService {

	@Autowired
	private VendedorRepository vendedorRepository;
	
	public Vendedor save(Long id, String cpf, String nome, BigDecimal salario) {
		Vendedor vendedor = new Vendedor(id, cpf, nome, salario);
		return vendedorRepository.save(vendedor);
	}
	
	public Optional<Vendedor> findByNome(String nome) {		
		return vendedorRepository.findByNome(nome); 
	}
}
