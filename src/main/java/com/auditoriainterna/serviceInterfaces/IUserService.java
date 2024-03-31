package com.auditoriainterna.serviceInterfaces;


import java.io.FileNotFoundException;
import java.util.List;

import com.auditoriainterna.dto.UserDto;
import com.auditoriainterna.model.User;

import net.sf.jasperreports.engine.JRException;

public interface IUserService {
	User save(UserDto userDto);
	public  List<User> listarUsers();
	User getUserById(Long id);
    void saveUser(User user);
    void deleteUser(Long id);
	byte[] exportPdf() throws JRException, FileNotFoundException;
    byte[] exportXls() throws JRException, FileNotFoundException;
}
