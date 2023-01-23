package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemData {
	private Integer id;
	private Integer orderId;
	private String barcode;
	private String productName;
	private Integer quantity;
	private Double sellingPrice;
	private Double mrp;


}