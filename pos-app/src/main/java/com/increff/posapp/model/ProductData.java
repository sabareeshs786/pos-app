package com.increff.posapp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductData extends ProductForm {

	Integer id;
	Integer brandCategory;
	Integer quantity;

	@Override
	public String toString() {
		return "ProductData [id=" + id + ", brand_category=" + brandCategory + ", toString()=" + super.toString()
				+ ", getBarcode()=" + getBarcode() + ", getBrand()=" + getBrand() + ", getCategory()=" + getCategory()
				+ ", getName()=" + getName() + ", getMrp()=" + getMrp() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}

}
