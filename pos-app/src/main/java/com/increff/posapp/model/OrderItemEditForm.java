package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter
@Setter
public class OrderItemEditForm {

	@Min(value = 1)
	@Max(value = Integer.MAX_VALUE)
	private Integer id;
	@Size(max = 20)
	private String barcode;
	@Min(value = 1)
	@Max(value = Integer.MAX_VALUE)
	private Integer quantity;
	@Max(value = Integer.MAX_VALUE)
	@Min(value = 0)
	private Double sellingPrice;
}