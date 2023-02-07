package com.increff.posapp.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.service.InventoryService;
import com.increff.posapp.util.Converter;
import com.increff.posapp.util.FormNormalizer;
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
import com.increff.posapp.util.FormValidator;

@Component
public class ProductDto {

	private static final Logger logger = Logger.getLogger(ProductDto.class);
	@Autowired
	private ProductService productService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private BrandService brandService;
	
	public void add(ProductForm form) throws ApiException {
		FormValidator.productFormValidator(form);
		FormNormalizer.productFormNormalizer(form);
		BrandPojo brandPojo = brandService.getByBrandAndCategory(form.getBrand(), form.getCategory());
		Integer brandCategory = brandPojo.getId();
		ProductPojo productPojo = Converter.convertToProductPojo(form, brandCategory);
		productService.add(productPojo);
		logger.info("Product added");
		InventoryPojo inventoryPojo = new InventoryPojo();
		inventoryPojo.setProductId(productPojo.getId());
		inventoryPojo.setQuantity(0);
		inventoryService.add(inventoryPojo);
		logger.info("Inventory updated");
	}

	public Page<ProductData> getById(Integer id) throws ApiException {
		ProductPojo productPojo = productService.getById(id);
		BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
		List<ProductData> list = new ArrayList<>();
		list.add(Converter.convertToProductData(productPojo, brandPojo));
		return new PageImpl<>(list, PageRequest.of(0, 1), 1);
	}
	
	public ProductData getByBarcode(String barcode) throws ApiException {
		ProductPojo productPojo = productService.getByBarcode(barcode);
		BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
		return Converter.convertToProductData(productPojo, brandPojo);
	}
	
	public Page<ProductData> getAll(Integer page, Integer size) throws ApiException{
		Page<ProductPojo> pojoPage = productService.getAllByPage(page, size);
		List<ProductPojo> list = pojoPage.getContent();
		List<ProductData> list2 = new ArrayList<>();
		for (ProductPojo productPojo : list) {
			BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
			list2.add(Converter.convertToProductData(productPojo, brandPojo));
		}
		Page<ProductData> dataPage = new PageImpl<>(list2, PageRequest.of(page, size), pojoPage.getTotalElements());
		return dataPage;
	}
	
	public void updateById(Integer id, ProductForm form) throws ApiException {
		FormValidator.productFormValidator(form);
		FormNormalizer.productFormNormalizer(form);
		BrandPojo brandPojo = brandService.getByBrandAndCategory(form.getBrand(), form.getCategory());
		ProductPojo p = Converter.convertToProductPojo(form, brandPojo.getId());
		productService.updateById(id, p);
	}

	public Page<ProductData> getData(Integer id, Integer page, Integer size) throws ApiException {
		if(id == null && page != null && size != null){
			return getAll(page, size);
		}
		else if (id != null){
			return getById(id);
		}
		else {
			throw new ApiException("Invalid request");
		}
	}
}
