package com.auditoriainterna.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auditoriainterna.model.Empleado;
import com.auditoriainterna.repositories.EmpleadoRepository;
import com.auditoriainterna.serviceInterfaces.IEmpleadoService;


@Service
public class EmpleadoService implements IEmpleadoService{

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public List<Empleado> obtenerEmpleadosPorArea(String area) {
        return empleadoRepository.findByArea(area);
    }

	@Override
	public List<Empleado> listaEmpleado() {	
		return empleadoRepository.findAll();
	}

	@Override
	public Empleado obtenerEmpleadoPorDni(int dni) {
		return empleadoRepository.findByDNI(dni);
	}
    
}