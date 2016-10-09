package com.example.cxjminfodemo.dto;

public class User {
	public int _id;
	public String username;
	public String password;

	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
