package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SalesReportData{
	private String startDate;
	private String endDate;
	private List<String> brands;
	private List<String> categories;
	private List<Integer> quantities;
	private List<String> totalAmounts;
	private String grandTotal;
}