package com.auditoriainterna.serviceInterfaces;

import java.util.List;

import com.auditoriainterna.model.Empleado;


public interface IEmpleadoService {
	public abstract List<Empleado> listaEmpleado();
	public Empleado obtenerEmpleadoPorDni(int dni);
}
