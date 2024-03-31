package com.auditoriainterna.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name="users",uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String username;
	
	private String password;
	
	private String role;
	
	private String fullname;
	
	
	public User() {
		super();
	}

	public User(String username, String password, String role, String fullname) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.fullname = fullname;
	}
	
	
	public User(Long id, String username, String role, String fullname) {
		super();
		this.id = id;
		this.username = username;
		this.role = role;
		this.fullname = fullname;
	}

	public User(Long id, String username, String password, String role, String fullname) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.fullname = fullname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	
	
	
}
