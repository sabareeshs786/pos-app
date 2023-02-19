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

	public Page<ProductData> getById(Integer id) throws ApiException {
		Validator.isEmpty("Id", id);
		ProductPojo productPojo = productService.getById(id);
		BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
		InventoryPojo inventoryPojo = new InventoryPojo();
		try{
			inventoryPojo = inventoryService.getByProductId(productPojo.getId());
		}
		catch (ApiException ex){
			inventoryPojo.setProductId(null);
			inventoryPojo.setQuantity(null);
		}
		List<ProductData> list = new ArrayList<>();
		list.add(Converter.convertToProductData(productPojo, brandPojo, inventoryPojo));
		return new PageImpl<>(list, PageRequest.of(0, 1), 1);
	}
	
	public ProductData getByBarcode(String barcode) throws ApiException {
		Validator.stringValidator("Barcode", barcode);
		barcode = StringUtil.toLowerCase(barcode);
		ProductPojo productPojo = productService.getByBarcode(barcode);
		BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
		InventoryPojo inventoryPojo = new InventoryPojo();
		try {
			inventoryPojo =inventoryService.getByProductId(productPojo.getId());
		}
		catch (ApiException ex){
			inventoryPojo.setProductId(null);
			inventoryPojo.setQuantity(null);
		}
		return Converter.convertToProductData(productPojo, brandPojo, inventoryPojo);
	}
	
	public Page<ProductData> getAll(Integer page, Integer size) throws ApiException{
		Validator.validatePageAndSize(page, size);
		Page<ProductPojo> pojoPage = productService.getAllByPage(page, size);
		List<ProductPojo> list = pojoPage.getContent();
		List<ProductData> list2 = new ArrayList<>();
		for (ProductPojo productPojo : list) {
			BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
			InventoryPojo inventoryPojo = new InventoryPojo();
			try{
				inventoryPojo = inventoryService.getByProductId(productPojo.getId());
			} catch (ApiException ex){
				inventoryPojo.setProductId(null);
				inventoryPojo.setQuantity(null);
			}
			finally {
				list2.add(Converter.convertToProductData(productPojo, brandPojo, inventoryPojo));
			}
		}
		return new PageImpl<>(list2, PageRequest.of(page, size), pojoPage.getTotalElements());
	}
	
	public ProductData updateById(Integer id, ProductForm form) throws ApiException, IllegalAccessException {
		Validator.isEmpty("Id", id);
		Validator.validate(form);
		Normalizer.normalize(form);
		BrandPojo brandPojo = brandService.getByBrandAndCategory(form.getBrand(), form.getCategory());
		ProductPojo productPojo = Converter.convertToProductPojo(form, brandPojo.getId());
		productPojo.setId(id);
		InventoryPojo inventoryPojo = new InventoryPojo();
		try{
			inventoryPojo = inventoryService.getByProductId(id);
		}
		catch (ApiException ex){
			inventoryPojo.setProductId(null);
			inventoryPojo.setQuantity(null);
		}
		return Converter.convertToProductData(productService.updateById(id, productPojo), brandPojo, inventoryPojo);
	}

	public Page<ProductData> getData(Integer id, Integer page, Integer size) throws ApiException {
		if(id == null && page != null && size != null){
			return getAll(page, size);
		}
		if (id != null){
			return getById(id);
		}
		throw new ApiException("Invalid request");
	}
}
