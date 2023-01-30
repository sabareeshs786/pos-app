package com.increff.posapp.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.posapp.model.ProductData;
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
import com.increff.posapp.util.ConverterDto;
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
		InventoryPojo inventoryPojo = ConverterDto.convertToInventoryPojo(form, productId);
		inventoryService.add(inventoryPojo);
	}

	public InventoryData getById(Integer id) throws ApiException {
		InventoryPojo inventoryPojo = inventoryService.getById(id);
		ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
		return ConverterDto.convertToInventoryData(inventoryPojo, productPojo.getBarcode());
	}

	public List<InventoryData> getAll() throws ApiException{
		List<InventoryPojo> listInventoryPojo = inventoryService.getAll();
		List<InventoryData> list = new ArrayList<InventoryData>();
		for(InventoryPojo inventoryPojo: listInventoryPojo) {
			ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
			String barcode = productPojo.getBarcode();
			list.add(ConverterDto.convertToInventoryData(inventoryPojo, barcode));
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
			list.add(ConverterDto.convertToInventoryData(inventoryPojo, barcode));
		}
		Page<InventoryData> dataPage = new PageImpl<>(list, PageRequest.of(page, size), pojoPage.getTotalElements());
		return dataPage;
	}
	
	public void updateById(Integer id, InventoryForm form) throws ApiException {
		FormValidator.inventoryFormValidator(form);
		FormNormalizer.inventoryFormNormalizer(form);
		ProductPojo productPojo = productService.getByBarcode(form.getBarcode());
		Integer productId = productPojo.getId();
		InventoryPojo inventoryPojo = ConverterDto.convertToInventoryPojo(form, productId);
		inventoryService.updateById(id, inventoryPojo);
	}

	protected ProductService productService(){
		return productService;
	}

	protected InventoryService inventoryService(){
		return inventoryService;
	}
}
