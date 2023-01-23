package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryForm {

	private String barcode;
	private Integer quantity;
	
	@Override
	public String toString() {
		return "InventoryForm [barcode=" + barcode + ", quantity=" + quantity + "]";
	}

}