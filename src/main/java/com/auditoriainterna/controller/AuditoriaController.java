package com.auditoriainterna.controller;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.auditoriainterna.model.Auditoria;
import com.auditoriainterna.model.Sala;
import com.auditoriainterna.serviceInterfaces.IAuditoriaService;
import com.auditoriainterna.serviceInterfaces.ISalaService;

@Controller
public class AuditoriaController {

	@Autowired
	private ISalaService iSalaService;
	
	@Autowired
	private IAuditoriaService iAuditoriaService;
	
	@Autowired
	UserDetailsService userdetailsService;
	
	@GetMapping("/auditoria")
	public String auditoria(Model model, Principal principal) {
		UserDetails userDetails = userdetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		List<Auditoria> auditorias=iAuditoriaService.listaAuditorias();
		List<Sala> salas=iSalaService.listaSalas();
		model.addAttribute("salas", salas);
		model.addAttribute("auditorias", auditorias);
		//model.addAttribute("auditoria", new Auditoria());
		return "auditoria";
	}
	
	@RequestMapping("/cargaSala")
	@ResponseBody
	public List<Sala> listaSala(){
		return iSalaService.listaSalas();
	}
	
	@RequestMapping("/cargaAuditoria")
	@ResponseBody
	public List<Auditoria> listaAuditoria(){
		return iAuditoriaService.listaAuditorias();
	}
	
	@GetMapping("/auditoria/guardar")
    public String formularioAuditoria(Model model) {
        List<Sala> salas = iSalaService.listaSalas();
        model.addAttribute("salas", salas);
        model.addAttribute("auditoria", new Auditoria()); // Crear una instancia vacía para el formulario
        return "formAuditoria";
    }
    
    @PostMapping("/auditoria/guardar")
    public String guardarAuditoria(@ModelAttribute("auditoria") Auditoria auditoria) {
    	iAuditoriaService.crearAuditoria(auditoria);   	
        return "redirect:/auditoria"; // Redirige a la lista de auditorías después de guardar
    }
    
    @GetMapping("/auditoria/editar/{id}")
    public String editarAuditoria(@PathVariable int id, Model model) {
    	Optional<Auditoria> auditoria=iAuditoriaService.listarId(id);
    	model.addAttribute("auditoria", auditoria);
    	List<Sala> salas = iSalaService.listaSalas();
        model.addAttribute("salas", salas);
    	return "formAuditoria";
    }
    
    @GetMapping("/auditoria/eliminar/{id}")
    public String eliminar(Model model,@PathVariable int id) {
    	iAuditoriaService.eliminar(id);
    	return "redirect:/auditoria";
    }
}
