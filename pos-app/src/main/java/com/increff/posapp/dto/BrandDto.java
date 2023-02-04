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

import com.increff.posapp.model.BrandData;
import com.increff.posapp.model.BrandForm;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.BrandService;
import com.increff.posapp.util.FormValidator;

@Component
public class BrandDto {
	@Autowired
	private BrandService brandService;
	
	public void add(BrandForm form) throws ApiException {
		FormValidator.brandFormValidator(form);
		FormNormalizer.brandFormNormalizer(form);
		BrandPojo brandPojo = Converter.convertToBrandPojo(form);
		brandService.add(brandPojo);
	}

	public Page<BrandData> get(Integer id) throws ApiException {
		BrandPojo brandPojo = brandService.getById(id);
		List<BrandData> listBrandData = new ArrayList<>();
		listBrandData.add(Converter.convertToBrandData(brandPojo));
		Page<BrandData> dataPage = new PageImpl<>(listBrandData, PageRequest.of(0,1), 1);
		return dataPage;
	}
	
	public List<BrandData> getAll() throws ApiException{
		List<BrandPojo> listBrandPojo = brandService.getAll();
		List<BrandData> listBrandData = new ArrayList<>();
		for(BrandPojo p: listBrandPojo) {
			listBrandData.add(Converter.convertToBrandData(p));
		}
		return listBrandData;
	}


	public Page<BrandData> getAll(Integer page, Integer size){
		Page<BrandPojo> pojoPage = brandService.getAllByPage(page, size);
		List<BrandPojo> brandPojoList = pojoPage.getContent();
		List<BrandData> listBrandData = new ArrayList<>();
		for(BrandPojo p: brandPojoList) {
			listBrandData.add(Converter.convertToBrandData(p));
		}
		Page<BrandData> dataPage = new PageImpl<>(listBrandData, PageRequest.of(page, size), pojoPage.getTotalElements());
		return dataPage;
	}

//	public List<BrandData> getByBrand(String brand) throws ApiException {
//		List<BrandPojo> listBrandPojo = brandService.getByBrand(brand);
//		List<BrandData> listBrandData = new ArrayList<>();
//		for (BrandPojo p : listBrandPojo) {
//			listBrandData.add(Converter.convertToBrandData(p));
//		}
//		return listBrandData;
//	}

//	public List<BrandData> getByCategory(String category) throws ApiException {
//		List<BrandPojo> listBrandPojo = brandService.getByCategory(category);
//		List<BrandData> listBrandData = new ArrayList<>();
//		for (BrandPojo p : listBrandPojo) {
//			listBrandData.add(Converter.convertToBrandData(p));
//		}
//		return listBrandData;
//	}

	public BrandData getByBrandandCategory(String brand, String category) throws ApiException {
		BrandPojo brandPojo = brandService.getByBrandAndCategory(brand, category);
		return Converter.convertToBrandData(brandPojo);
	}

	public void updateById(Integer id, BrandForm form) throws ApiException {
		FormValidator.brandFormValidator(form);
		FormNormalizer.brandFormNormalizer(form);
		BrandPojo p = Converter.convertToBrandPojo(form);
		brandService.updateById(id, p);
	}
}
