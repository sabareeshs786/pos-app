package com.increff.posapp.util;

import com.increff.posapp.model.*;
import com.increff.posapp.service.ApiException;

import java.lang.reflect.Field;

public class Validator {

	public static void orderFormValidator(OrderForm form) throws ApiException {
		Integer len = form.getBarcodes().size();
		for(Integer i=0; i < len; i++){
			if(StringUtil.isEmpty(form.getBarcodes().get(i))){
				throw new ApiException("Barcode cannot be empty");
			}
			if(StringUtil.isNotAlNum(form.getBarcodes().get(i))){
				throw new ApiException("Only alpha-numeric characters are allowed in the entries");
			}
			if(form.getQuantities().get(i) == null){
				throw new ApiException("Quantity can't be empty");
			}
			if(form.getQuantities().get(i) <= 0){
				throw new ApiException("Quantity must be greater than zero");
			}
			if(form.getSellingPrices().get(i) == null){
				throw new ApiException("Selling price must be greater than zero");
			}
			if(form.getSellingPrices().get(i) <= 0){
				throw new ApiException("Selling Price must be greater than zero");
			}
		}
	}
	public static void orderItemEditFormValidator(OrderItemEditForm form) throws ApiException {
		if (StringUtil.isEmpty(form.getBarcode())) {
			throw new ApiException("Barcode cannot be empty");
		}
		if(StringUtil.isNotAlNum(form.getBarcode())){
			throw new ApiException("Only alpha-numeric characters are allowed in the entries");
		}
		if (form.getQuantity() == null) {
			throw new ApiException("Quantity cannot be empty");
		}
		if (form.getSellingPrice() == null) {
			throw new ApiException("Selling price cannot be empty");
		}
		if (form.getQuantity() <= 0) {
			throw new ApiException("Quantity must be greater than zero");
		}
		if(form.getSellingPrice() <= 0.0){
			throw new ApiException("Selling price must be greater than zero");
		}
	}

	public static void stringValidator(String field, String s) throws ApiException {
		if(StringUtil.isEmpty(s)){
			throw new ApiException(field + " can't be empty");
		}
	}

	public static void validatePageAndSize(Integer page, Integer size) throws ApiException {
		if(page == null){
			throw new ApiException("Page value is empty");
		}
		if(size == null){
			throw new ApiException("Size is empty");
		}
	}

	public static <T> void isEmpty(String field, T val) throws ApiException {
		if(val == null){
			throw new ApiException(field + " can't be empty");
		}
	}
	public static void validate(Object o) throws IllegalAccessException, ApiException {
		Field[] fields = o.getClass().getDeclaredFields();
		for(Field field: fields){
			field.setAccessible(true);
			if(field.get(o) == null){
				throw new ApiException(field.getName().substring(0,1).toUpperCase() +
						field.getName().substring(1) +
						" can't be empty");
			}
			if(field.getType().getSimpleName().equals("String")){
				String val = (String) field.get(o);
				if(val.isEmpty()){
					throw new ApiException(field.getName().substring(0,1).toUpperCase() +
							field.getName().substring(1) +
							" can't be empty");
				}
				if(StringUtil.isNotAlNum(val)){
					throw new ApiException(field.getName().substring(0,1).toUpperCase() +
							field.getName().substring(1) +
							" should contain only alpha-numeric characters");
				}
			}
			field.setAccessible(false);
		}
	}

}
