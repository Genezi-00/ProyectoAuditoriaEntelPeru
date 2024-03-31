package com.auditoriainterna.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auditoriainterna.model.Auditoria;
import com.auditoriainterna.repositories.AuditoriaRepository;
import com.auditoriainterna.serviceInterfaces.IAuditoriaService;

@Service
public class AuditoriaService implements IAuditoriaService{

	@Autowired
	private AuditoriaRepository auditoriaRepository;
	
	@Override
	public List<Auditoria> listaAuditorias() {
		return auditoriaRepository.findAll();
	}

	@Override
	public Auditoria crearAuditoria(Auditoria a) {
		return auditoriaRepository.save(a);
	}

	@Override
	public Optional<Auditoria> listarId(int id) {
		return auditoriaRepository.findById(id);
	}

	@Override
	public void eliminar(int id) {
		auditoriaRepository.deleteById(id);
		
	}

}
