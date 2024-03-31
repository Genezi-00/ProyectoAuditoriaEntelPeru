package com.auditoriainterna.controller;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auditoriainterna.dto.UserDto;
import com.auditoriainterna.model.User;
import com.auditoriainterna.repositories.UserRepository;
import com.auditoriainterna.serviceInterfaces.IUserService;

import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

	@Autowired
	UserDetailsService userdetailsService;

	@Autowired
	private IUserService userService;

	@Autowired
	private UserRepository userRepository;
	

	@GetMapping("/registration")
	public String getRegistroPage(@ModelAttribute("user") UserDto userDto) {
		return "registro";
	}

	@PostMapping("/registration")
	public String saveUser(@ModelAttribute("user") UserDto userDto, Model model, Principal principal) {
		userService.save(userDto);
		model.addAttribute("message", "Registro Correcto");
		return "registro";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/inicio-page")
	public String user(Model model, Principal principal) {
		UserDetails userDetails = userdetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("user", userDetails);
		return "inicio";
	}
	
	@GetMapping("/usuarios")
	public String index(Model model, Principal principal) {
		UserDetails userDetails = userdetailsService.loadUserByUsername(principal.getName());
		model.addAttribute("userP", userDetails);
		List<User> userList = userService.listarUsers();
		model.addAttribute("userList", userList);
		model.addAttribute("user", new User());
		return "users";
	}

	@PostMapping("/usuarios/guardar")
	public String saveUser(@ModelAttribute User user) {
		userService.saveUser(user);
		return "redirect:/usuarios";
	}

	@GetMapping("/usuarios/eliminar/{userId}")
	public String deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return "redirect:/usuarios";
	}

	@GetMapping("/usuarios/buscarUno/{userId}")
	@ResponseBody
	public ResponseEntity<User> findOne(@PathVariable Long userId) {
		User user = userService.getUserById(userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping("/export-pdf")
	public ResponseEntity<byte[]> exportPdf() throws JRException, FileNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("petsReport", "usuariosReporte.pdf");
		return ResponseEntity.ok().headers(headers).body(userService.exportPdf());
	}

	@GetMapping("/export-xls")
	public ResponseEntity<byte[]> exportXls() throws JRException, FileNotFoundException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8");
		var contentDisposition = ContentDisposition.builder("attachment").filename("usuariosReporte" + ".xls").build();
		headers.setContentDisposition(contentDisposition);
		return ResponseEntity.ok().headers(headers).body(userService.exportXls());
	}
}
