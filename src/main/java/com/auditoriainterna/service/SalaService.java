package com.auditoriainterna.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auditoriainterna.model.Sala;
import com.auditoriainterna.repositories.SalaRepository;
import com.auditoriainterna.serviceInterfaces.ISalaService;

@Service
public class SalaService implements ISalaService{

	@Autowired
	private SalaRepository salaRepository;
	
	@Override
	public List<Sala> listaSalas() {
		return salaRepository.findAll();
	}

}
