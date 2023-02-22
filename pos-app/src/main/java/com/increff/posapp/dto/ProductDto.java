package com.increff.posapp.dto;

import com.increff.posapp.model.ProductData;
import com.increff.posapp.model.ProductForm;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.BrandService;
import com.increff.posapp.service.InventoryService;
import com.increff.posapp.service.ProductService;
import com.increff.posapp.util.Converter;
import com.increff.posapp.util.Normalizer;
import com.increff.posapp.util.StringUtil;
import com.increff.posapp.util.Validator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDto {
	@Autowired
	private ProductService productService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private BrandService brandService;
	private Logger logger = Logger.getLogger(ProductDto.class);

	public ProductData add(ProductForm form) throws ApiException, IllegalAccessException {
		Validator.validate(form);
		Normalizer.normalize(form);
		BrandPojo brandPojo = brandService.getByBrandAndCategory(form.getBrand(), form.getCategory());
		Integer brandCategory = brandPojo.getId();
		ProductPojo productPojo = Converter.convertToProductPojo(form, brandCategory);
		ProductPojo p = productService.add(productPojo);
		InventoryPojo inventoryPojo = new InventoryPojo();
		inventoryPojo.setQuantity(null);
		return Converter.convertToProductData(p, brandPojo, inventoryPojo);
	}

	public ProductData get(Integer id) throws ApiException {
		Validator.validate("Id", id);
		ProductPojo productPojo = productService.getById(id);
		BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
		InventoryPojo inventoryPojo = getInventoryStatus(productPojo);
		return Converter.convertToProductData(productPojo, brandPojo, inventoryPojo);
	}

	public Object get(String barcode, Integer page, Integer size) throws ApiException {
		if(barcode == null && page != null && size != null){
			return getAll(page, size);
		}
		if(barcode != null){
			return getByBarcode(barcode);
		}
		throw new ApiException("Invalid request");
	}

	public ProductData update(Integer id, ProductForm form) throws ApiException, IllegalAccessException {
		Validator.validate("Id", id);
		Validator.validate(form);
		Normalizer.normalize(form);
		BrandPojo brandPojo = brandService.getByBrandAndCategory(form.getBrand(), form.getCategory());
		ProductPojo productPojo = Converter.convertToProductPojo(form, brandPojo.getId());
		productPojo.setId(id);
		InventoryPojo inventoryPojo = getInventoryStatus(productPojo);
		return Converter.convertToProductData(productService.updateById(id, productPojo), brandPojo, inventoryPojo);
	}

	private ProductData getByBarcode(String barcode) throws ApiException {
		Validator.validate("Barcode", barcode);
		barcode = StringUtil.toLowerCase(barcode);
		ProductPojo productPojo = productService.getByBarcode(barcode);
		BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
		InventoryPojo inventoryPojo = getInventoryStatus(productPojo);
		return Converter.convertToProductData(productPojo, brandPojo, inventoryPojo);
	}
	
	private Page<ProductData> getAll(Integer page, Integer size) throws ApiException{
		Validator.validate("Page",page);
		Validator.validate("Size", size);
		List<ProductPojo> productPojoList =  productService.getAll(page, size);
		List<BrandPojo> brandPojoList = new ArrayList<>();
		List<InventoryPojo> inventoryPojoList = new ArrayList<>();
		for (ProductPojo productPojo : productPojoList) {
			BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
			brandPojoList.add(brandPojo);
			InventoryPojo inventoryPojo = getInventoryStatus(productPojo);
			inventoryPojoList.add(inventoryPojo);
		}
		List<ProductData> productDataList = Converter.convertToProductDataList(productPojoList, brandPojoList, inventoryPojoList);
		return new PageImpl<>(productDataList, PageRequest.of(page, size), productService.getTotalElements());
	}

	private InventoryPojo getInventoryStatus(ProductPojo productPojo){
		InventoryPojo inventoryPojo = new InventoryPojo();
		try{
			logger.info("Executing try block");
			inventoryPojo = inventoryService.getByProductId(productPojo.getId());
		}
		catch (ApiException ex){
			inventoryPojo.setProductId(null);
			inventoryPojo.setQuantity(null);
		}
		return inventoryPojo;
	}
}
