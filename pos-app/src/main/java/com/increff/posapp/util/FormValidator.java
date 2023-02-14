package com.increff.posapp.util;

import com.increff.posapp.model.*;
import com.increff.posapp.service.ApiException;

public class FormValidator {

	public static void orderFormValidator(OrderForm form) throws ApiException {
		Integer len = form.getBarcodes().size();
		for(Integer i=0; i < len; i++){
			if(form.getBarcodes().get(i) == null || form.getBarcodes().get(i).isEmpty()){
				throw new ApiException("Barcode cannot be empty");
			}
			if(form.getQuantities().get(i) == null || form.getQuantities().get(i) <= 0){
				throw new ApiException("Quantity must be greater than zero");
			}
			if(form.getSellingPrices().get(i) == null || form.getSellingPrices().get(i) <= 0){
				throw new ApiException("Selling Price must be greater than zero");
			}
		}
	}

	public static void orderItemEditFormValidator(OrderItemEditForm form) throws ApiException {
		if (form.getBarcode() == null || form.getBarcode().isEmpty()) {
			throw new ApiException("Barcode cannot be empty");
		}
		if (form.getSellingPrice() == null) {
			throw new ApiException("Selling price cannot be empty");
		}
		if (form.getQuantity() == null) {
			throw new ApiException("Quantity cannot be empty");
		}

		if (form.getQuantity() <= 0) {
			throw new ApiException("Quantity must be greater than zero");
		}

		if (form.getSellingPrice() <= 0.0) {
			throw new ApiException("Selling price must be greater than zero");
		}
	}
}
