package com.auditoriainterna.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auditoriainterna.model.Empleado;
import com.auditoriainterna.service.EmpleadoService;
import com.auditoriainterna.serviceInterfaces.IEmpleadoService;

@Controller
public class EmpleadoController {

	@Autowired
	private IEmpleadoService iEmpleadoService;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	UserDetailsService userdetailsService;

	@GetMapping("/areas")
	public String mostrarAreas(Principal principal, Model model) {
		UserDetails userDetails = userdetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		return "areas";
	}

	@GetMapping("/empleados")
	public String mostrarEmpleadosPorArea(@RequestParam String area, Model model, Principal principal) {
		UserDetails userDetails = userdetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		List<Empleado> empleados = empleadoService.obtenerEmpleadosPorArea(area);
		model.addAttribute("empleados", empleados);
		return "empleados";
	}
	
	@RequestMapping("/cargaEmpleados")
	@ResponseBody
	public List<Empleado> listaEmpleados(){
		return iEmpleadoService.listaEmpleado();
	}
	
	
	
	
	
}