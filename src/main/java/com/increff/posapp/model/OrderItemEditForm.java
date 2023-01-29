package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemEditForm {

	private Integer id;
	private String barcode;
	private Integer quantity;
	private Double sellingPrice;
}