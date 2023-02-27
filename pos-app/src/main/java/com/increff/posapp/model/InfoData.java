package com.increff.posapp.model;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class InfoData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message;
	private String email;
	private String role;

	public InfoData() {
		message = "no message";
		email = "no email";
	}

	@Override
	public String toString() {
		return "{\"message\": \"" + message + "\", " +
				"\"email\" : \"" + email + "\", " +
				"\"role\": \""+ role + "\"}";
	}
}
