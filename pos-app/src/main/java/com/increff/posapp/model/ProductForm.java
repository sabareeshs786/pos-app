package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProductForm {
	private String barcode;
	private String brand;
	private String category;
	private String name;
	private Double mrp;
	
	@Override
	public String toString() {
		return "ProductForm [barcode=" + barcode + ", brand=" + brand + ", category=" + category + ", name=" + name
				+ ", mrp=" + mrp + "]";
	}
	
}
