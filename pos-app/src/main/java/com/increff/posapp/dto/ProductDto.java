package com.increff.posapp.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.service.InventoryService;
import com.increff.posapp.util.Converter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.increff.posapp.model.ProductData;
import com.increff.posapp.model.ProductForm;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.BrandService;
import com.increff.posapp.service.ProductService;

@Component
public class ProductDto {

	private static final Logger logger = Logger.getLogger(ProductDto.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private BrandService brandService;
	
	public ProductData add(ProductForm form) throws ApiException {
		BrandPojo brandPojo = brandService.getByBrandAndCategory(form.getBrand(), form.getCategory());
		Integer brandCategory = brandPojo.getId();
		ProductPojo productPojo = Converter.convertToProductPojo(form, brandCategory);
		ProductPojo p = productService.add(productPojo);
		logger.info("Product added");
		InventoryPojo inventoryPojo = new InventoryPojo();
		inventoryPojo.setProductId(productPojo.getId());
		inventoryPojo.setQuantity(0);
		InventoryPojo pojo = inventoryService.add(inventoryPojo);
		logger.info("Inventory updated with 0");
		return Converter.convertToProductData(p, brandPojo, pojo);
	}

	public Page<ProductData> getById(Integer id) throws ApiException {
		ProductPojo productPojo = productService.getById(id);
		BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
		InventoryPojo inventoryPojo = inventoryService.getByProductId(productPojo.getId());
		List<ProductData> list = new ArrayList<>();
		list.add(Converter.convertToProductData(productPojo, brandPojo, inventoryPojo));
		return new PageImpl<>(list, PageRequest.of(0, 1), 1);
	}
	
	public ProductData getByBarcode(String barcode) throws ApiException {
		ProductPojo productPojo = productService.getByBarcode(barcode);
		BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
		InventoryPojo inventoryPojo = inventoryService.getByProductId(productPojo.getId());
		return Converter.convertToProductData(productPojo, brandPojo, inventoryPojo);
	}
	
	public Page<ProductData> getAll(Integer page, Integer size) throws ApiException{
		Page<ProductPojo> pojoPage = productService.getAllByPage(page, size);
		List<ProductPojo> list = pojoPage.getContent();
		List<ProductData> list2 = new ArrayList<>();
		for (ProductPojo productPojo : list) {
			BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
			InventoryPojo inventoryPojo = inventoryService.getByProductId(productPojo.getId());
			list2.add(Converter.convertToProductData(productPojo, brandPojo, inventoryPojo));
		}
		return new PageImpl<>(list2, PageRequest.of(page, size), pojoPage.getTotalElements());
	}
	
	public ProductData updateById(Integer id, ProductForm form) throws ApiException {
		BrandPojo brandPojo = brandService.getByBrandAndCategory(form.getBrand(), form.getCategory());
		ProductPojo productPojo = Converter.convertToProductPojo(form, brandPojo.getId());
		productPojo.setId(id);
		InventoryPojo inventoryPojo = inventoryService.getByProductId(productPojo.getId());
		return Converter.convertToProductData(productService.updateById(id, productPojo), brandPojo, inventoryPojo);
	}

	public Page<ProductData> getData(Integer id, Integer page, Integer size) throws ApiException {
		if(id == null && page != null && size != null){
			return getAll(page, size);
		}
		else if (id != null){
			logger.info("In getData. Id = "+id);
			return getById(id);
		}
		else {
			throw new ApiException("Invalid request");
		}
	}
}
