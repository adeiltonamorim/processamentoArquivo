package com.amorim.processador.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amorim.processador.domain.Vendedor;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
	
	Optional<Vendedor> findByNome(String nome);

}
