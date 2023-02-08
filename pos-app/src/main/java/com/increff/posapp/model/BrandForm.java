package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
@Getter
@Setter
public class BrandForm {
	@Size(max = 20)
	private String brand;
	@Size(max = 20)
	private String category;

}
