package com.auditoriainterna.serviceInterfaces;

import java.util.List;
import java.util.Optional;

import com.auditoriainterna.model.Auditoria;


public interface IAuditoriaService {
	public abstract List<Auditoria> listaAuditorias();
	public Auditoria crearAuditoria(Auditoria a);
	public Optional<Auditoria> listarId(int id);
	public void eliminar(int id);
}
