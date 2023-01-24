package com.increff.posapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
		return getCheckByBrand(brand);
	}

	public List<BrandPojo> getByCategory(String category) throws ApiException {
		return getCheckByCategory(category);
	}

	public BrandPojo getByBrandAndCategory(String brand, String category) throws ApiException {
		return getCheckByBrandAndCategory(brand, category);
	}

	public List<BrandPojo> getAll() throws ApiException {
		return brandDao.selectAll();
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

	public List<BrandPojo> getCheckByBrand(String brand) throws ApiException {
		List<BrandPojo> p = brandDao.selectByBrand(brand);
		if (p == null) {
			throw new ApiException("The given brand doesn't exist");
		}
		return p;
	}

	public List<BrandPojo> getCheckByCategory(String category) throws ApiException {
		List<BrandPojo> p = brandDao.selectByCategory(category);
		if (p == null) {
			throw new ApiException("The given category doesn't exist");
		}
		return p;
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
		if(brandDao.selectByBrandAndCategory(p.getBrand(), p.getCategory()) != null) {
			throw new ApiException("The entered brand and category combination already exists\nEnter a different brand or category");
		}
	}
}
