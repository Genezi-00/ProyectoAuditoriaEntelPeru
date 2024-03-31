package com.auditoriainterna.service;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.auditoriainterna.model.Entrevista;
import com.auditoriainterna.repositories.EntrevistaRepository;
import com.auditoriainterna.serviceInterfaces.IEntrevistaService;
import com.auditoriainterna.util.reportes.EntrevistaReportGenerator;

import net.sf.jasperreports.engine.JRException;

@Service
public class EntrevistaService implements IEntrevistaService {

	
	
	@Autowired
	private EntrevistaRepository entrevistaRepository;

	@Autowired
	EntrevistaReportGenerator entrevistaReportGenerator; 
	
	@Override
	public List<Entrevista> listaEntrevistas() {
		return entrevistaRepository.findAll();
	}


	@Override
	public List<Entrevista> obtenerEntrevistasPorCodigoAuditoria(int codigoAuditoria) {
		return entrevistaRepository.findByCodigoAuditoria(codigoAuditoria);
	}


	@Override
	public Entrevista crearEntrevista(Entrevista e) {
		return entrevistaRepository.save(e);
	}


	@Override
	public Optional<Entrevista> listaId(int id) {
		return entrevistaRepository.findById(id);
	}


	@Override
	public void eliminar(int id) {
		entrevistaRepository.deleteById(id);
		
	}


	@Override
	public List<Entrevista> obtenerEntrevistasPorDni(int dni) {
		 return entrevistaRepository.findByDni(dni);
	}

	public void aprobarEntrevista(int id) {
        // Recupera la entrevista por ID
        Optional<Entrevista> optionalEntrevista = entrevistaRepository.findById(id);
        
        if (optionalEntrevista.isPresent()) {
            // Modifica el estado a "Aprobado"
            Entrevista entrevista = optionalEntrevista.get();
            entrevista.setEstado("Aprobado");
            entrevistaRepository.save(entrevista);
        }
    }

    public void rechazarEntrevista(int id) {
        // Recupera la entrevista por ID
        Optional<Entrevista> optionalEntrevista = entrevistaRepository.findById(id);
        
        if (optionalEntrevista.isPresent()) {
            // Modifica el estado a "Rechazado"
            Entrevista entrevista = optionalEntrevista.get();
            entrevista.setEstado("Rechazado");
            entrevistaRepository.save(entrevista);
        }
    }


	@Override
	public byte[] exportPdf(int id,Map<String, Object> params) throws JRException, FileNotFoundException {
	    Optional<Entrevista> entrevistaOptional = entrevistaRepository.findById(id);

	    if (entrevistaOptional.isPresent()) {
	        Entrevista entrevista = entrevistaOptional.get();
	        List<Entrevista> entrevistaList = Collections.singletonList(entrevista);

	        return entrevistaReportGenerator.exportToPdf(entrevistaList,params);
	    } else {
	        // Manejar el caso en el que la entrevista no se encuentra
	        throw new NoSuchElementException("Entrevista no encontrada para el ID: " + id);
	    }
	}


	@Override
	public byte[] exportXls(int id,Map<String, Object> params) throws JRException, FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

    
	



	

	

	
}
