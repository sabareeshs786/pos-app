package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryData extends InventoryForm{

	private Integer id;

	@Override
	public String toString() {
		return "InventoryData [id=" + id + ", toString()=" + super.toString() + ", getBarcode()=" + getBarcode()
				+ ", getQuantity()=" + getQuantity() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ "]";
	}
	
}