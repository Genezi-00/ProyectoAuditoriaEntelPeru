package com.auditoriainterna.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Sala {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "codigo_sala")
	private int codigo_sala;
	
	@Column(name = "nombre_sala")
	private String nombre_sala;
	
	@Column(name = "capacidad")
	private int capacidad;
	
	
	public int getCodigo_sala() {
		return codigo_sala;
	}
	public void setCodigo_sala(int codigo_sala) {
		this.codigo_sala = codigo_sala;
	}
	public String getNombre_sala() {
		return nombre_sala;
	}
	public void setNombre_sala(String nombre_sala) {
		this.nombre_sala = nombre_sala;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	
	
}
