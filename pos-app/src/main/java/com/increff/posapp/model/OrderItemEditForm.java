package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
public class OrderItemEditForm {

	private Integer id;
	private String barcode;
	private Integer quantity;
	private Double sellingPrice;
}