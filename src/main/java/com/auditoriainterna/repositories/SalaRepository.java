package com.auditoriainterna.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auditoriainterna.model.Sala;

public interface SalaRepository extends JpaRepository<Sala, Integer> {

	
}
