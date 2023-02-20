package com.increff.posapp.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.posapp.util.Converter;
import com.increff.posapp.util.Normalizer;
import com.increff.posapp.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.increff.posapp.model.InventoryData;
import com.increff.posapp.model.InventoryForm;
import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.InventoryService;
import com.increff.posapp.service.ProductService;

@Component
public class InventoryDto {
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private ProductService productService;
	
	public InventoryData add(InventoryForm form) throws ApiException, IllegalAccessException {
		Validator.validate(form);
		Normalizer.normalize(form);
		ProductPojo productPojo = productService.getByBarcode(form.getBarcode());
		Integer productId = productPojo.getId();
		InventoryPojo inventoryPojo = Converter.convertToInventoryPojo(form, productId);
		return Converter.convertToInventoryData(inventoryService.add(inventoryPojo),productPojo.getBarcode());
	}

	private Page<InventoryData> getByProductId(Integer productId) throws ApiException {
		Validator.isEmpty("Product id", productId);
		InventoryPojo inventoryPojo = inventoryService.getByProductId(productId);
		ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
		List<InventoryData> list = new ArrayList<InventoryData>();
		list.add(Converter.convertToInventoryData(inventoryPojo, productPojo.getBarcode()));
		return new PageImpl<>(list, PageRequest.of(0,1), 1);
	}

	private Page<InventoryData> getAll(Integer page, Integer size) throws ApiException{
		Validator.isEmpty("Page", page);
		Validator.isEmpty("Size", size);
		Page<InventoryPojo> pojoPage = inventoryService.getAllByPage(page, size);
		List<InventoryPojo> listInventoryPojo = pojoPage.getContent();
		List<InventoryData> list = new ArrayList<InventoryData>();
		for(InventoryPojo inventoryPojo: listInventoryPojo) {
			ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
			String barcode = productPojo.getBarcode();
			list.add(Converter.convertToInventoryData(inventoryPojo, barcode));
		}
		return new PageImpl<>(list, PageRequest.of(page, size), pojoPage.getTotalElements());
	}
	
	public InventoryData updateByProductId(Integer id, InventoryForm form) throws ApiException, IllegalAccessException {
		Validator.isEmpty("Id", id);
		Validator.validate(form);
		Normalizer.normalize(form);
		ProductPojo productPojo = productService.getByBarcode(form.getBarcode());
		Integer productId = productPojo.getId();
		InventoryPojo inventoryPojo = Converter.convertToInventoryPojo(form, productId);
		return Converter.convertToInventoryData(
				inventoryService.updateByProductId(inventoryPojo),
				productPojo.getBarcode()
		);
	}

	public Page<InventoryData> getData(Integer productId, Integer page, Integer size) throws ApiException {
		if(productId == null && page != null && size != null){
			return getAll(page, size);
		}
		else if (productId != null){
			return getByProductId(productId);
		}
		else {
			throw new ApiException("Invalid request");
		}
	}
}
