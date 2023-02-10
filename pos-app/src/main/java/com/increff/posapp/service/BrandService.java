package com.increff.posapp.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.posapp.model.BrandData;
import com.increff.posapp.util.Converter;
import com.increff.posapp.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.increff.posapp.dao.BrandDao;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.util.StringUtil;
@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandService {

	@Autowired
	private BrandDao brandDao;

	public BrandData add(BrandPojo p) throws ApiException {
		normalize(p);
		validate(p);
		return Converter.convertToBrandData(brandDao.insert(p));
	}

	public BrandPojo getById(int id) throws ApiException {
		return getCheckById(id);
	}

	public List<BrandPojo> getByBrand(String brand) throws ApiException {
		brand = validateAndNormalizeString(brand, "Brand");
		return getCheckByBrand(brand);
	}

	public List<BrandPojo> getByCategory(String category) throws ApiException {
		category = validateAndNormalizeString(category, "Category");
		return getCheckByCategory(category);
	}

	public Page<BrandPojo> getByBrand(String brand, Integer page, Integer size) throws ApiException {
		brand = validateAndNormalizeString(brand, "Brand");
		if(!ValidationUtil.isPageSizeValid(page, size))
			throw new ApiException("Invalid page number or size");
		return getCheckByBrand(brand, page, size);
	}

	public Page<BrandPojo> getByCategory(String category, Integer page, Integer size) throws ApiException {
		category = validateAndNormalizeString(category, "Category");
		if(!ValidationUtil.isPageSizeValid(page, size))
			throw new ApiException("Invalid page number or size");
		return getCheckByCategory(category, page, size);
	}

	public BrandPojo getByBrandAndCategory(String brand, String category) throws ApiException {
		brand = validateAndNormalizeString(brand, "Brand");
		category = validateAndNormalizeString(category, "Category");
		return getCheckByBrandAndCategory(brand, category);
	}

	public List<BrandPojo> getAll() throws ApiException {
		return brandDao.selectAll();
	}

	public Page<BrandPojo> getAllByPage(Integer page, Integer size) throws ApiException {
		if(!ValidationUtil.isPageSizeValid(page, size))
			throw new ApiException("Invalid page number or size");
		return brandDao.getAllByPage(page, size);
	}
	public BrandData updateById(int id, BrandPojo p) throws ApiException {
		normalize(p);
		validate(p);
		BrandPojo ex = getCheckById(id);
		ex.setBrand(p.getBrand());
		ex.setCategory(p.getCategory());
		brandDao.update(ex);
		return Converter.convertToBrandData(ex);
	}

	private BrandPojo getCheckById(int id) throws ApiException {
		BrandPojo p = brandDao.selectById(id);
		if (p == null) {
			throw new ApiException("Brand Category combination with given ID does not exit");
		}
		return p;
	}

	private Page<BrandPojo> getCheckByBrand(String brand, Integer page, Integer size) throws ApiException {
		Page<BrandPojo> p = brandDao.selectByBrand(brand, page, size);
		if (p.getTotalElements() == 0) {
			throw new ApiException("The given brand doesn't exist");
		}
		return p;
	}

	private List<BrandPojo> getCheckByBrand(String brand) throws ApiException {
		List<BrandPojo> pojos = brandDao.selectByBrand(brand);
		if(pojos.size() == 0){
			throw new ApiException("No such brand");
		}
		return pojos;
	}

	private Page<BrandPojo> getCheckByCategory(String category, Integer page, Integer size) throws ApiException {
		Page<BrandPojo> p = brandDao.selectByCategory(category, page, size);
		if (p.getTotalElements() == 0) {
			throw new ApiException("The given category doesn't exist");
		}
		return p;
	}

	private List<BrandPojo> getCheckByCategory(String category) throws ApiException {
		List<BrandPojo> pojos = brandDao.selectByCategory(category);
		if(pojos.size() == 0){
			throw new ApiException("No such category");
		}
		return pojos;
	}

	private BrandPojo getCheckByBrandAndCategory(String brand, String category) throws ApiException {
		BrandPojo p = brandDao.selectByBrandAndCategory(brand, category);
		if (p == null) {
			throw new ApiException("The given brand and category combination doesn't exist");
		}
		return p;
	}
	
	private void normalize(BrandPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
		p.setCategory(StringUtil.toLowerCase(p.getCategory()));
	}

	private void validate(BrandPojo p) throws ApiException {
		if(StringUtil.isEmpty(p.getBrand())){
			throw new ApiException("Brand can't be empty");
		}

		if(StringUtil.isEmpty(p.getCategory())){
			throw new ApiException("Category can't be empty");
		}

		if(brandDao.selectByBrandAndCategory(p.getBrand(), p.getCategory()) != null) {
			throw new ApiException("The entered brand and category combination already exists\nEnter a different brand or category");
		}
	}

	private String validateAndNormalizeString(String s, String field) throws ApiException {
		s = StringUtil.toLowerCase(s);
		if(s == null || s.length() == 0){
			throw new ApiException(field + " is empty");
		}
		return s;
	}
}
