package com.increff.posapp.util;

import com.increff.posapp.model.*;
import com.increff.posapp.service.ApiException;

public class FormNormalizer {
	public static void brandFormNormalizer(BrandForm form) throws ApiException {
		form.setBrand(form.getBrand().toLowerCase());
		form.setCategory(form.getCategory().toLowerCase());
	}

	public static void productFormNormalizer(ProductForm form) throws ApiException {
		form.setBarcode(form.getBarcode().toLowerCase());
		form.setBrand(form.getBrand().toLowerCase());
		form.setCategory(form.getCategory().toLowerCase());
		form.setName(form.getName().toLowerCase());
		form.setMrp(Double.parseDouble(DoubleUtil.roundToString(form.getMrp())));
	}

	public static void inventoryFormNormalizer(InventoryForm form) throws ApiException {
		form.setBarcode(form.getBarcode().toLowerCase());
	}

	public static void orderItemEditFormNormalizer(OrderItemEditForm form) throws ApiException {
		form.setBarcode(form.getBarcode().toLowerCase());
		form.setSellingPrice(DoubleUtil.round(form.getSellingPrice(), 2));
	}
}
