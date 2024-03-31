package com.auditoriainterna.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.auditoriainterna.model.Auditoria;
import com.auditoriainterna.model.Entrevista;
import java.util.Date;


@Repository
public interface EntrevistaRepository extends JpaRepository<Entrevista, Integer>{
	@Query(value = "SELECT * FROM Entrevista WHERE codigo_auditoria = :codigoAuditoria", nativeQuery = true)
    List<Entrevista> findByCodigoAuditoria(@Param("codigoAuditoria") int codigoAuditoria);
	
	@Query(value = "SELECT * FROM Entrevista WHERE dni = :dni", nativeQuery = true)
    List<Entrevista> findByDni(@Param("dni") int dni);
}
