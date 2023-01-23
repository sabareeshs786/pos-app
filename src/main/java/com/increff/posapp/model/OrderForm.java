package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderForm {

	private String barcode;
	private Integer quantity;
	private Double sellingPrice;

	@Override
	public String toString() {
		return "OrderForm [barcode=" + barcode + ", quantity=" + quantity + ", sellingPrice=" + sellingPrice + "]";
	}

}