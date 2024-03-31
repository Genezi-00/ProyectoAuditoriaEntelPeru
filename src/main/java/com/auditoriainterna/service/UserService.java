package com.auditoriainterna.service;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auditoriainterna.dto.UserDto;
import com.auditoriainterna.model.User;
import com.auditoriainterna.repositories.UserRepository;
import com.auditoriainterna.serviceInterfaces.IUserService;
import com.auditoriainterna.util.reportes.UserReportGenerator;

import net.sf.jasperreports.engine.JRException;

@Service
public class UserService implements IUserService{

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserReportGenerator userReportGenerator;
	
	@Override
	public User save(UserDto userDto) {
		User user = new User(userDto.getUsername(),passwordEncoder.encode(userDto.getPassword()),userDto.getRole(),userDto.getFullname());
		return userRepository.save(user);
	}

	@Override
	public List<User> listarUsers() {
		return userRepository.findAll();
	}

	@Override
	public byte[] exportPdf() throws JRException, FileNotFoundException {
		return userReportGenerator.exportToPdf(userRepository.findAll());
	}

	@Override
	public byte[] exportXls() throws JRException, FileNotFoundException {
		return userReportGenerator.exportToXls(userRepository.findAll());
	}

	@Override
	public User getUserById(Long id) {
		 return userRepository.findById(id).orElse(null);
	}

	/*@Override
	public void saveUser(User user) {
		User u=new User(user.getId(),user.getUsername(),user.getPassword(),user.getRole(),user.getFullname());
		userRepository.save(u);
		
	}*/

	@Override
	public void saveUser(User user) {
	    // Recupera el usuario existente de la base de datos
	    User existingUser = userRepository.findById(user.getId()).orElse(null);

	    // Si es un usuario existente y la contraseña no ha cambiado, mantén la contraseña sin encriptar
	    if (existingUser != null && user.getPassword().equals(existingUser.getPassword())) {
	        user.setPassword(existingUser.getPassword());
	    } else {
	        // Si la contraseña ha cambiado o es un nuevo usuario, encripta la nueva contraseña
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	    }

	    // Guarda el usuario en la base de datos
	    userRepository.save(user);
	}
	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
		
	}

}
