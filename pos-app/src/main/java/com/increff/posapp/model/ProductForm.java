package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProductForm {

	@Size(max = 20)
	private String barcode;
	@Size(max = 20)
	private String brand;
	@Size(max = 20)
	private String category;
	@Size(max = 30)
	private String name;
	@Max(value = Integer.MAX_VALUE)
	private Double mrp;
	
	@Override
	public String toString() {
		return "ProductForm [barcode=" + barcode + ", brand=" + brand + ", category=" + category + ", name=" + name
				+ ", mrp=" + mrp + "]";
	}
	
}
