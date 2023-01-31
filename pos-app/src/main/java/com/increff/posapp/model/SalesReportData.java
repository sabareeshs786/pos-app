package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SalesReportData{
	private String startDate;
	private String  endDate;
	private List<String> brands;
	private List<String> categories;
	private List<Integer> quantities;
	private List<String> totalAmounts;
	private String totalRevenue;

	public SalesReportData(){
		this.brands = new ArrayList<String>();
		this.categories = new ArrayList<String>();
		this.quantities = new ArrayList<Integer>();
		this.totalAmounts = new ArrayList<String>();
	}

	@Override
	public String toString() {
		return "SalesReportData{" +
				"startDate=" + startDate +
				", endDate=" + endDate +
				", brands=" + brands +
				", categories=" + categories +
				", quantities=" + quantities +
				", totalAmounts=" + totalAmounts +
				", totalRevenue='" + totalRevenue + '\'' +
				'}';
	}
}