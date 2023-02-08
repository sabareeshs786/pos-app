package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class MessageData {

	@Size(min = 10, max = 50)
	private String message;

}
