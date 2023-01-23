package com.increff.posapp.util;

import com.increff.posapp.model.*;
import com.increff.posapp.service.ApiException;

public class FormValidator {
	public static void brandFormValidator(BrandForm form) throws ApiException {
		if (form.getBrand().toString().isEmpty()) {
			throw new ApiException("Brand cannot be empty");
		}
		if (form.getCategory().toString().isEmpty()) {
			throw new ApiException("Category cannot be empty");
		}
	}

	public static void productFormValidator(ProductForm form) throws ApiException {
		if (form.getBarcode().isEmpty()) {
			throw new ApiException("Barcode cannot be empty");
		}
		if (form.getBrand().isEmpty()) {
			throw new ApiException("Brand cannot be empty");
		}
		if (form.getCategory().isEmpty()) {
			throw new ApiException("Category cannot be empty");
		}
		if (form.getMrp().toString().isEmpty()) {
			throw new ApiException("MRP cannot be empty");
		}
		if (Double.parseDouble(form.getMrp()) <= 0) {
			throw new ApiException("MRP must be greater than zero");
		}
		if (form.getName().isEmpty()) {
			throw new ApiException("Product name cannot be empty");
		}
	}

	public static void inventoryFormValidator(InventoryForm form) throws ApiException {
		if (form.getBarcode().isEmpty()) {
			throw new ApiException("Barcode cannot be empty");
		}
		if (form.getQuantity().toString().isEmpty()) {
			throw new ApiException("Quantity cannot be empty");
		}
		if (form.getQuantity() <= 0) {
			throw new ApiException("Quantity must be greater than zero");
		}
	}

	public static void orderFormValidator(OrderForm form) throws ApiException {
		if (form.getBarcode().isEmpty()) {
			throw new ApiException("Barcode cannot be empty");
		}
		if (form.getSellingPrice().toString().isEmpty()) {
			throw new ApiException("Selling price cannot be empty");
		}
		if(form.getSellingPrice() < 0){
			throw new ApiException("Selling price can't be less than zero");
		}
		if (form.getQuantity().toString().isEmpty()) {
			throw new ApiException("Quantity cannot be empty");
		}

		if (form.getQuantity() <= 0) {
			throw new ApiException("Quantity must be grater than zero");
		}
	}

	public static void orderItemEditFormValidator(OrderItemEditForm form) throws ApiException {
		if (form.getBarcode().isEmpty()) {
			throw new ApiException("Barcode cannot be empty");
		}
		if (form.getSellingPrice().toString().isEmpty()) {
			throw new ApiException("Selling price cannot be empty");
		}
		if (form.getQuantity().toString().isEmpty()) {
			throw new ApiException("Quantity cannot be empty");
		}

		if (form.getQuantity() <= 0) {
			throw new ApiException("Quantity must be grater than zero");
		}
	}
}
