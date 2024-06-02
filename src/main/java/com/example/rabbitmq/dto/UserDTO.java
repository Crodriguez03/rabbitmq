package com.example.rabbitmq.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {
	
	private static final long serialVersionUID = -2426365553123996616L;
	
	private String id;
	private String name;
	private String surname;
	
	public UserDTO() {
	}

	public UserDTO(String id, String name, String surname) {
		this.id = id;
		this.name = name;
		this.surname = surname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

}
