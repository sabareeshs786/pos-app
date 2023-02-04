package com.increff.posapp.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.posapp.util.Converter;
import com.increff.posapp.util.FormNormalizer;
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
import com.increff.posapp.util.FormValidator;

@Component
public class InventoryDto {
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private ProductService productService;
	
	public void add(InventoryForm form) throws ApiException {
		FormValidator.inventoryFormValidator(form);
		FormNormalizer.inventoryFormNormalizer(form);
		ProductPojo productPojo = productService.getByBarcode(form.getBarcode());
		Integer productId = productPojo.getId();
		InventoryPojo inventoryPojo = Converter.convertToInventoryPojo(form, productId);
		inventoryService.add(inventoryPojo);
	}

	public Page<InventoryData> getByProductId(Integer productId) throws ApiException {
		InventoryPojo inventoryPojo = inventoryService.getByProductId(productId);
		ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
		List<InventoryData> list = new ArrayList<InventoryData>();
		list.add(Converter.convertToInventoryData(inventoryPojo, productPojo.getBarcode()));
		return new PageImpl<>(list, PageRequest.of(0,1), 1);
	}

	public List<InventoryData> getAll() throws ApiException{
		List<InventoryPojo> listInventoryPojo = inventoryService.getAll();
		List<InventoryData> list = new ArrayList<InventoryData>();
		for(InventoryPojo inventoryPojo: listInventoryPojo) {
			ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
			String barcode = productPojo.getBarcode();
			list.add(Converter.convertToInventoryData(inventoryPojo, barcode));
		}
		return list;
	}

	public Page<InventoryData> getAll(Integer page, Integer size) throws ApiException{
		Page<InventoryPojo> pojoPage = inventoryService.getAllByPage(page, size);
		List<InventoryPojo> listInventoryPojo = pojoPage.getContent();
		List<InventoryData> list = new ArrayList<InventoryData>();
		for(InventoryPojo inventoryPojo: listInventoryPojo) {
			ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
			String barcode = productPojo.getBarcode();
			list.add(Converter.convertToInventoryData(inventoryPojo, barcode));
		}
		Page<InventoryData> dataPage = new PageImpl<>(list, PageRequest.of(page, size), pojoPage.getTotalElements());
		return dataPage;
	}
	
	public void updateByProductId(Integer id, InventoryForm form) throws ApiException {
		FormValidator.inventoryFormValidator(form);
		FormNormalizer.inventoryFormNormalizer(form);
		ProductPojo productPojo = productService.getByBarcode(form.getBarcode());
		Integer productId = productPojo.getId();
		InventoryPojo inventoryPojo = Converter.convertToInventoryPojo(form, productId);
		inventoryService.updateByProductId(productId, inventoryPojo);
	}

	protected ProductService productService(){
		return productService;
	}

	protected InventoryService inventoryService(){
		return inventoryService;
	}
}
