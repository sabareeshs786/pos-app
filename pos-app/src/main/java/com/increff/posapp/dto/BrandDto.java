package com.increff.posapp.dto;

import java.util.ArrayList;
import java.util.List;

import com.increff.posapp.util.Converter;
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
	
	public BrandData add(BrandForm form) throws ApiException {
		BrandPojo brandPojo = Converter.convertToBrandPojo(form);
		return brandService.add(brandPojo);
	}

	public Page<BrandData> getData(Integer id, Integer page, Integer size) throws ApiException {
		if(id == null && page != null && size != null){
			return getAll(page, size);
		}
		else if (id != null){
			return get(id);
		}
		else {
			throw new ApiException("Invalid request");
		}
	}

	private Page<BrandData> get(Integer id) throws ApiException {
		BrandPojo brandPojo = brandService.getById(id);
		List<BrandData> listBrandData = new ArrayList<>();
		listBrandData.add(Converter.convertToBrandData(brandPojo));
		return new PageImpl<>(listBrandData, PageRequest.of(0,1), 1);
	}
	
//	private List<BrandData> getAll() throws ApiException{
//		List<BrandPojo> listBrandPojo = brandService.getAll();
//		List<BrandData> listBrandData = new ArrayList<>();
//		for(BrandPojo p: listBrandPojo) {
//			listBrandData.add(Converter.convertToBrandData(p));
//		}
//		return listBrandData;
//	}


	private Page<BrandData> getAll(Integer page, Integer size) throws ApiException {
		Page<BrandPojo> pojoPage = brandService.getAllByPage(page, size);
		List<BrandPojo> brandPojoList = pojoPage.getContent();
		List<BrandData> listBrandData = new ArrayList<>();
		for(BrandPojo p: brandPojoList) {
			listBrandData.add(Converter.convertToBrandData(p));
		}
		return new PageImpl<>(listBrandData, PageRequest.of(page, size), pojoPage.getTotalElements());
	}

	public BrandData updateById(Integer id, BrandForm form) throws ApiException {
		BrandPojo p = Converter.convertToBrandPojo(form);
		return brandService.updateById(id, p);
	}
}
