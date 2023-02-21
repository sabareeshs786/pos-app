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

import com.increff.posapp.model.BrandData;
import com.increff.posapp.model.BrandForm;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.BrandService;

@Component
public class BrandDto {
	@Autowired
	private BrandService brandService;
	
	public BrandData add(BrandForm form) throws ApiException, IllegalAccessException {
		Validator.validate(form);
		Normalizer.normalize(form);
		BrandPojo brandPojo = Converter.convertToBrandPojo(form);
		return brandService.add(brandPojo);
	}

	public Page<BrandData> getData(Integer id, Integer page, Integer size) throws ApiException {
		if(id == null && page != null && size != null){
			return getAll(page, size);
		}
		if (id != null){
			return get(id);
		}
		throw new ApiException("Invalid request");
	}

	public BrandData updateById(Integer id, BrandForm form) throws ApiException, IllegalAccessException {
		Validator.isEmpty("Id", id);
		Validator.validate(form);
		Normalizer.normalize(form);
		BrandPojo p = Converter.convertToBrandPojo(form);
		return brandService.updateById(id, p);
	}

	// Private methods
	private Page<BrandData> get(Integer id) throws ApiException {
		Validator.isEmpty("Id", id);
		BrandPojo brandPojo = brandService.getById(id);
		List<BrandData> listBrandData = new ArrayList<>();
		listBrandData.add(Converter.convertToBrandData(brandPojo));
		return new PageImpl<>(listBrandData, PageRequest.of(0,1), 1);
	}

	private Page<BrandData> getAll(Integer page, Integer size) throws ApiException {
		List<BrandPojo> brandPojoList = brandService.getAll(page, size);
		List<BrandData> listBrandData = new ArrayList<>();
		for(BrandPojo p: brandPojoList) {
			listBrandData.add(Converter.convertToBrandData(p));
		}
		return new PageImpl<>(listBrandData, PageRequest.of(page, size), brandService.getTotalElements());
	}
}
