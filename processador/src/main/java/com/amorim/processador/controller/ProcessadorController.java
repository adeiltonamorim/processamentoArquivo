package com.amorim.processador.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amorim.processador.domain.Cliente;
import com.amorim.processador.repository.ClienteRepository;

@RestController
@RequestMapping(value = "/workers")
public class ProcessadorController {

	@Autowired
	private ClienteRepository workerRepository;
	
	@GetMapping
	public ResponseEntity<List<Cliente>> findAll() {
		List<Cliente> lista = workerRepository.findAll();
		return ResponseEntity.ok(lista);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Cliente> findById(@PathVariable Long id) {
		return workerRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
	
}
