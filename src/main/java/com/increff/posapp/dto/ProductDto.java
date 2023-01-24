package com.increff.posapp.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.posapp.util.FormNormalizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.posapp.model.ProductData;
import com.increff.posapp.model.ProductForm;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.BrandService;
import com.increff.posapp.service.ProductService;
import com.increff.posapp.util.ConverterDto;
import com.increff.posapp.util.FormValidator;

@Component
public class ProductDto {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	public void add(ProductForm form) throws ApiException {
		FormValidator.productFormValidator(form);
		FormNormalizer.productFormNormalizer(form);
		BrandPojo brandPojo = brandService.getByBrandAndCategory(form.getBrand(), form.getCategory());
		Integer brandCategory = brandPojo.getId();
		ProductPojo productPojo = ConverterDto.convertToProductPojo(form, brandCategory);
		productService.add(productPojo);
	}

	public ProductData getById(Integer id) throws ApiException {
		ProductPojo productPojo = productService.getById(id);
		BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
		return ConverterDto.convertToProductData(productPojo, brandPojo);
	}
	
	public List<ProductData> getAll() throws ApiException{
		List<ProductPojo> list = productService.getAll();
		List<ProductData> list2 = new ArrayList<>();
		for (ProductPojo productPojo : list) {
			BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
			list2.add(ConverterDto.convertToProductData(productPojo, brandPojo));
		}
		System.out.println(list2);
		return list2;
	}
	
	public void updateById(Integer id, ProductForm form) throws ApiException {
		FormValidator.productFormValidator(form);
		FormNormalizer.productFormNormalizer(form);
		BrandPojo brandPojo = brandService.getByBrandAndCategory(form.getBrand(), form.getCategory());
		ProductPojo p = ConverterDto.convertToProductPojo(form, brandPojo.getId());
		productService.updateById(id, p);
	}
}
