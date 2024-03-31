package com.auditoriainterna.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Empleado {
	    @Id
	    private int DNI;
	    private String nombreEmpleado;
	    private String apellidoEmpleado;
	    
	    @Temporal(TemporalType.DATE)
		@DateTimeFormat(iso = ISO.DATE)
	    private Date fechaNac;
	    private String area;
	    private String cargo;
	    
		

		public Empleado() {
			
		}

		public Empleado(int dNI, String nombreEmpleado, String apellidoEmpleado, Date fechaNac, String area) {
			DNI = dNI;
			this.nombreEmpleado = nombreEmpleado;
			this.apellidoEmpleado = apellidoEmpleado;
			this.fechaNac = fechaNac;
			this.area = area;
		}

		public int getDNI() {
			return DNI;
		}

		public void setDNI(int dNI) {
			DNI = dNI;
		}

		public String getNombreEmpleado() {
			return nombreEmpleado;
		}

		public void setNombreEmpleado(String nombreEmpleado) {
			this.nombreEmpleado = nombreEmpleado;
		}

		public String getApellidoEmpleado() {
			return apellidoEmpleado;
		}

		public void setApellidoEmpleado(String apellidoEmpleado) {
			this.apellidoEmpleado = apellidoEmpleado;
		}

		public Date getFechaNac() {
			return fechaNac;
		}

		public void setFechaNac(Date fechaNac) {
			this.fechaNac = fechaNac;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}
		
		public String getCargo() {
			return cargo;
		}

		public void setCargo(String cargo) {
			this.cargo = cargo;
		}
	    
	}



