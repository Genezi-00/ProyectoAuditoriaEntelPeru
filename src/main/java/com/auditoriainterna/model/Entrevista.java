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
public class Entrevista {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cod_entrevista;

	@Column(columnDefinition = "TEXT")
	private String observaciones;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso = ISO.DATE)
	private Date fecha;

	@Column(name = "pregunta1")
	private String pregunta1;

	@Column(name = "pregunta2")
	private String pregunta2;

	@Column(name = "pregunta3")
	private String pregunta3;

	@Column(name = "estado")
	private String estado;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dni")
	private Empleado empleado;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "codigoAuditoria")
	private Auditoria auditoria;

	/*Metodos para el Jasper Report*/
	public String getNombreEmpleado() {
		return this.empleado.getNombreEmpleado()+" "+this.empleado.getApellidoEmpleado();
	}

	public Integer getDNI() {
		return this.empleado.getDNI();
	}
	
	public String getArea() {
		return this.empleado.getArea();
	}
	
	public String getCargo() {
		return this.empleado.getCargo();
	}
	/*Fin de Metodos para Jasper Report*/
	

	/*Metodos de Entrevista*/
	public int getCod_entrevista() {
		return cod_entrevista;
	}

	public void setCod_entrevista(int cod_entrevista) {
		this.cod_entrevista = cod_entrevista;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Auditoria getAuditoria() {
		return auditoria;
	}

	public void setAuditoria(Auditoria auditoria) {
		this.auditoria = auditoria;
	}

	public String getPregunta1() {
		return pregunta1;
	}

	public void setPregunta1(String pregunta1) {
		this.pregunta1 = pregunta1;
	}

	public String getPregunta2() {
		return pregunta2;
	}

	public void setPregunta2(String pregunta2) {
		this.pregunta2 = pregunta2;
	}

	public String getPregunta3() {
		return pregunta3;
	}

	public void setPregunta3(String pregunta3) {
		this.pregunta3 = pregunta3;
	}

}
