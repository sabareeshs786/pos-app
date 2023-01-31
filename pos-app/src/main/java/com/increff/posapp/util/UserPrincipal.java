package com.increff.posapp.util;

public class UserPrincipal {

	private int id;
	private String email;
	private String role;

	public String getEmail() {
		return email;
	}

	public int getId() {
		return id;
	}
	public String getRole() { return role; }
	public void setId(int id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public void setRole(String role) {this.role = role; }

	@Override
	public String toString() {
		return "UserPrincipal{" +
				"id=" + id +
				", email='" + email + '\'' +
				", role='" + role + '\'' +
				'}';
	}
}