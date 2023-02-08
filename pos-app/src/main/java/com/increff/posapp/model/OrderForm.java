package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderForm {
	@Size(max = 20)
	private List<String> barcodes;
	@Max(value = Integer.MAX_VALUE)
	private List<Integer> quantities;
	@Max(value = Integer.MAX_VALUE)
	private List<Double> sellingPrices;

	public OrderForm(){
		this.barcodes = new ArrayList<String>();
		this.quantities = new ArrayList<Integer>();
		this.sellingPrices = new ArrayList<Double>();
	}

}