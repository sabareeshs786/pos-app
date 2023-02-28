package com.increff.posapp.model;

import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderData {
	
	private Integer id;
	private String time;
	private String totalAmount;
	private Boolean isInvoiced;
	
	@Override
	public String toString() {
		return "OrderData [id=" + id + ", time=" + time + "]";
	}
	
}
