package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {

	private String barcode;
	private String brand;
	private String category;
	private String name;
	private String mrp;
	
	@Override
	public String toString() {
		return "ProductForm [barcode=" + barcode + ", brand=" + brand + ", category=" + category + ", name=" + name
				+ ", mrp=" + mrp + "]";
	}
	
}
