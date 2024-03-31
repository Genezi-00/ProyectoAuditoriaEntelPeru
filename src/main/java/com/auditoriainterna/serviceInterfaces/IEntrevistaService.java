package com.auditoriainterna.serviceInterfaces;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.auditoriainterna.model.Entrevista;

import net.sf.jasperreports.engine.JRException;

public interface IEntrevistaService {
	public abstract List<Entrevista> listaEntrevistas();
	 public List<Entrevista> obtenerEntrevistasPorCodigoAuditoria(int codigoAuditoria);
	 public Entrevista crearEntrevista(Entrevista e);
	 public Optional<Entrevista> listaId(int id);
	 public void eliminar(int id);
	 public List<Entrevista> obtenerEntrevistasPorDni(int dni);
	 byte[] exportPdf(int id,Map<String, Object> params) throws JRException, FileNotFoundException;
	 byte[] exportXls(int id,Map<String, Object> params) throws JRException, FileNotFoundException;
}
