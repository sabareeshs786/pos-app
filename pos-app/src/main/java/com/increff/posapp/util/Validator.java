package com.increff.posapp.util;

import com.increff.posapp.model.*;
import com.increff.posapp.service.ApiException;

import java.lang.reflect.Field;

public class Validator {

	private Validator() {}

	public static void orderFormValidator(OrderForm form) throws ApiException {
		if((form.getBarcodes().size() != form.getQuantities().size()) || (form.getBarcodes().size() != form.getSellingPrices().size())){
			throw new ApiException("Input form contains missing values");
		}
		Integer len = form.getBarcodes().size();
		for(Integer i=0; i < len; i++){
			validate("Barcode", form.getBarcodes().get(i));
			validate("Quantity", form.getQuantities().get(i));
			validate("Selling price", form.getSellingPrices().get(i));
		}
	}

	public static <T> void validate(String field, T val) throws ApiException {
		if(val == null){
			throw new ApiException(field + " can't be empty");
		}
		if(val instanceof String){
			if(StringUtil.isEmpty((String) val)){
				throw new ApiException(field + " is empty");
			}
			if(StringUtil.isNotAlNum((String) val)){
				throw new ApiException(field + " should contain only alpha-numeric characters");
			}
		}
		else if(val instanceof Integer){
			if((Integer)val < 0){
				throw new ApiException(field + " can't be less than zero");
			}
		}
		else if(val instanceof Double){
			Double value = (Double) val;
			if(value < 0){
				throw new ApiException(field + " can't be less than zero");
			}
			if(value.isInfinite() || value.isNaN()){
				throw new ApiException(field + " is invalid");
			}
		}
		else {
			throw new ApiException("Invalid field type detected");
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
				if(StringUtil.isEmpty(val)){
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
