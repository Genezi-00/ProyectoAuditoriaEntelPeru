package com.auditoriainterna.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Auditoria {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigoAuditoria")
	private int codigoAuditoria;

	@Column(name = "nombre_auditoria")
	private String nombre_auditoria;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE)
	private Date fecha;
	
	@Column(columnDefinition = "TEXT")
	private String observaciones;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="codigo_sala")
	private Sala sala;
	
	public Auditoria() {
	
	}

	public Auditoria(int codigoAuditoria, String nombre_auditoria, Date fecha, String observaciones) {
		super();
		this.codigoAuditoria = codigoAuditoria;
		this.nombre_auditoria = nombre_auditoria;
		this.fecha = fecha;
		this.observaciones = observaciones;
	}

	public int getCodigo_auditoria() {
		return codigoAuditoria;
	}

	public void setCodigo_auditoria(int codigo_auditoria) {
		this.codigoAuditoria = codigo_auditoria;
	}

	public String getNombre_auditoria() {
		return nombre_auditoria;
	}

	public void setNombre_auditoria(String nombre_auditoria) {
		this.nombre_auditoria = nombre_auditoria;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Sala getSala() {
		return sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}
	
	
}
