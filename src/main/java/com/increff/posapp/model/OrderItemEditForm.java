package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemEditForm extends OrderForm{

	private Integer id;

	@Override
	public String toString() {
		super.toString();
		return "OrderItemEditForm{" +
				"id=" + id +
				'}';
	}
}