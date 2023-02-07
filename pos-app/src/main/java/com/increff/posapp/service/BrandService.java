package com.increff.posapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import com.increff.posapp.dao.BrandDao;
import com.increff.posapp.dao.InventoryDao;
import com.increff.posapp.dao.ProductDao;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.util.StringUtil;
@Service
@Transactional(rollbackOn = ApiException.class)
public class BrandService {

	@Autowired
	private BrandDao brandDao;

	public void add(BrandPojo p) throws ApiException {
		normalize(p);
		validate(p);
		brandDao.insert(p);
	}

	public BrandPojo getById(int id) throws ApiException {
		return getCheckById(id);
	}

	public List<BrandPojo> getByBrand(String brand) throws ApiException {
		brand = brand.toLowerCase();
		return getCheckByBrand(brand);
	}

	public List<BrandPojo> getByCategory(String category) throws ApiException {
		category = category.toLowerCase();
		return getCheckByCategory(category);
	}

	public Page<BrandPojo> getByBrand(String brand, Integer page, Integer size) throws ApiException {
		brand = brand.toLowerCase();
		return getCheckByBrand(brand, page, size);
	}

	public Page<BrandPojo> getByCategory(String category, Integer page, Integer size) throws ApiException {
		category = category.toLowerCase();
		return getCheckByCategory(category, page, size);
	}

	public BrandPojo getByBrandAndCategory(String brand, String category) throws ApiException {
		brand = brand.toLowerCase();
		category = category.toLowerCase();
		return getCheckByBrandAndCategory(brand, category);
	}

	public List<BrandPojo> getAll() throws ApiException {
		return brandDao.selectAll();
	}

	public Page<BrandPojo> getAllByPage(Integer page, Integer size){
		return brandDao.getAllByPage(page, size);
	}
	public void updateById(int id, BrandPojo p) throws ApiException {
		normalize(p);
		validate(p);
		BrandPojo ex = getCheckById(id);
		ex.setBrand(p.getBrand());
		ex.setCategory(p.getCategory());
		brandDao.update(ex);
	}

	public BrandPojo getCheckById(int id) throws ApiException {
		BrandPojo p = brandDao.selectById(id);
		if (p == null) {
			throw new ApiException("Brand Category combination with given ID does not exit");
		}
		return p;
	}

	public Page<BrandPojo> getCheckByBrand(String brand, Integer page, Integer size) throws ApiException {
		Page<BrandPojo> p = brandDao.selectByBrand(brand, page, size);
		if (p.getTotalElements() == 0) {
			throw new ApiException("The given brand doesn't exist");
		}
		return p;
	}

	public List<BrandPojo> getCheckByBrand(String brand) throws ApiException {
		List<BrandPojo> pojos = brandDao.selectByBrand(brand);
		if(pojos.size() == 0){
			throw new ApiException("No such brand");
		}
		return pojos;
	}

	public Page<BrandPojo> getCheckByCategory(String category, Integer page, Integer size) throws ApiException {
		Page<BrandPojo> p = brandDao.selectByCategory(category, page, size);
		if (p.getTotalElements() == 0) {
			throw new ApiException("The given category doesn't exist");
		}
		return p;
	}

	public List<BrandPojo> getCheckByCategory(String category) throws ApiException {
		List<BrandPojo> pojos = brandDao.selectByCategory(category);
		if(pojos.size() == 0){
			throw new ApiException("No such category");
		}
		return pojos;
	}

	public BrandPojo getCheckByBrandAndCategory(String brand, String category) throws ApiException {
		BrandPojo p = brandDao.selectByBrandAndCategory(brand, category);
		if (p == null) {
			throw new ApiException("The given brand and category combination doesn't exist");
		}
		return p;
	}
	
	protected static void normalize(BrandPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
		p.setCategory(StringUtil.toLowerCase(p.getCategory()));
	}

	protected void validate(BrandPojo p) throws ApiException {
		if(p.getBrand().isEmpty()){
			throw new ApiException("Brand can't be empty");
		}
		if(p.getCategory().isEmpty()){
			throw new ApiException("Category can't be empty");
		}
		if(brandDao.selectByBrandAndCategory(p.getBrand(), p.getCategory()) != null) {
			throw new ApiException("The entered brand and category combination already exists\nEnter a different brand or category");
		}
	}
}
