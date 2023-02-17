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
	private List<String> barcodes;
	private List<Integer> quantities;
	private List<Double> sellingPrices;

	public OrderForm(){
		this.barcodes = new ArrayList<String>();
		this.quantities = new ArrayList<Integer>();
		this.sellingPrices = new ArrayList<Double>();
	}

}