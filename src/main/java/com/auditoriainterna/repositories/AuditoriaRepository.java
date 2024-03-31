package com.auditoriainterna.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auditoriainterna.model.Auditoria;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer>{
	List<Auditoria> findByCodigoAuditoria(int codigo_auditoria);
	
}
