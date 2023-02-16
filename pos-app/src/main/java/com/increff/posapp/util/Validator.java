package com.increff.posapp.util;

import com.increff.posapp.model.*;
import com.increff.posapp.service.ApiException;

public class Validator {
	public static void brandFormValidator(BrandForm form) throws ApiException {
		if (StringUtil.isEmpty(form.getBrand())) {
			throw new ApiException("Brand cannot be empty");
		}
		if (StringUtil.isEmpty(form.getCategory())) {
			throw new ApiException("Category cannot be empty");
		}
		if(StringUtil.isNotAlNum(form.getBrand()) || StringUtil.isNotAlNum(form.getCategory())){
			throw new ApiException("Only alpha-numeric characters are allowed in the entries");
		}
	}

	public static void productFormValidator(ProductForm form) throws ApiException {
		if (StringUtil.isEmpty(form.getBarcode())) {
			throw new ApiException("Barcode cannot be empty");
		}
		if (StringUtil.isEmpty(form.getBrand())) {
			throw new ApiException("Brand cannot be empty");
		}
		if (StringUtil.isEmpty(form.getCategory())) {
			throw new ApiException("Category cannot be empty");
		}
		if (StringUtil.isEmpty(form.getName())) {
			throw new ApiException("Product name cannot be empty");
		}
		if(StringUtil.isNotAlNum(form.getBarcode()) ||
		   StringUtil.isNotAlNum(form.getBrand()) ||
		   StringUtil.isNotAlNum(form.getCategory()) ||
		   StringUtil.isNotAlNum(form.getName())){
			throw new ApiException("Only alpha-numeric characters are allowed in the entries");
		}
		if (form.getMrp() == null) {
			throw new ApiException("MRP cannot be empty");
		}
		if (form.getMrp() <= 0.0) {
			throw new ApiException("MRP must be greater than zero");
		}
	}

	public static void inventoryFormValidator(InventoryForm form) throws ApiException {
		if (StringUtil.isEmpty(form.getBarcode())) {
			throw new ApiException("Barcode cannot be empty");
		}
		if(StringUtil.isNotAlNum(form.getBarcode())){
			throw new ApiException("Only alpha-numeric characters are allowed in the entries");
		}
		if (form.getQuantity() == null) {
			throw new ApiException("Quantity cannot be empty");
		}
		if (form.getQuantity() <= 0) {
			throw new ApiException("Quantity must be greater than zero");
		}
	}

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

	public static void stringValidator(String s) throws ApiException {
		if(StringUtil.isEmpty(s)){
			throw new ApiException("Empty input field detected");
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

	public static void validateId(Integer id) throws ApiException {
		if(id == null){
			throw new ApiException("Id can't be empty");
		}
	}
}
