package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductData extends ProductForm {

	Integer id;
	Integer brand_category;
	Integer quantity;

	@Override
	public String toString() {
		return "ProductData [id=" + id + ", brand_category=" + brand_category + ", toString()=" + super.toString()
				+ ", getBarcode()=" + getBarcode() + ", getBrand()=" + getBrand() + ", getCategory()=" + getCategory()
				+ ", getName()=" + getName() + ", getMrp()=" + getMrp() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}

}
