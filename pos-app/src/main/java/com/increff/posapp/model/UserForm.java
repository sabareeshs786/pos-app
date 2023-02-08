package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class UserForm {

	@Size(min = 5, max = 20)
	private String email;
	@Size(min = 8, max = 50)
	private String password;
}
