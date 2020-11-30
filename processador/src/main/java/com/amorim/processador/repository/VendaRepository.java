package com.amorim.processador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.amorim.processador.domain.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

}
