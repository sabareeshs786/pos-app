package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportForm {

	private String startDate;
	private String endDate;
	private String brand;
	private String category;

	@Override
	public String toString() {
		return "SalesReportForm{" +
				"startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				", brand='" + brand + '\'' +
				", category='" + category + '\'' +
				'}';
	}
}