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
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private InventoryDao inventoryDao;

	public void add(BrandPojo p) throws ApiException {
		normalize(p);
		if(StringUtil.isEmpty(p.getBrand())) {
			throw new ApiException("Brand name cannot be empty");
		}
		if(StringUtil.isEmpty(p.getCategory())) {
			throw new ApiException("Brand category cannot be empty");
		}
		if(brandDao.selectByBrandAndCategory(p.getBrand(), p.getCategory()) != null) {
			throw new ApiException("The entered brand and category combination already exists\nEnter a different brand or category");
		}
		brandDao.insert(p);
	}

	public void deleteById(int id) throws ApiException {
		Integer brand_category = id;
		List<ProductPojo> productList = productDao.selectByBrandCategory(brand_category);
		for(ProductPojo p: productList) {
			inventoryDao.deleteByProductId(p.getId());
		}
		productDao.deleteByBrandCategory(brand_category);
		brandDao.deleteById(id);
	}

	public void deleteByBrand(String brand) throws ApiException {
		for(BrandPojo brandPojo: brandDao.selectByBrand(brand)) {
			for(ProductPojo productPojo: productDao.selectByBrandCategory(brandPojo.getId())) {
				inventoryDao.deleteByProductId(productPojo.getId());
				productDao.deleteById(productPojo.getId());
			}
		}
		brandDao.deleteByBrand(brand);
	}

	public void deleteByCategory(String category) throws ApiException{
		for(BrandPojo brandPojo: brandDao.selectByCategory(category)) {
			for(ProductPojo productPojo: productDao.selectByBrandCategory(brandPojo.getId())) {
				inventoryDao.deleteByProductId(productPojo.getId());
				productDao.deleteById(productPojo.getId());
			}
		}
		brandDao.deleteByCategory(category);
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
		if(brandDao.selectByBrandAndCategory(p.getBrand(), p.getCategory()) != null) {
			throw new ApiException("The entered brand and category combination already exists\\nEnter a different brand or category");
		}
		BrandPojo ex = getCheckById(id);
		ex.setBrand(p.getBrand());
		ex.setCategory(p.getCategory());
		brandDao.update(ex);
	}

	public void updateByBrand(String brand, BrandPojo p) throws ApiException {
		normalize(p);
		List<BrandPojo> list = getCheckByBrand(brand);
		for(BrandPojo pojo: list) {
			if(brandDao.selectByBrandAndCategory(p.getBrand(), p.getCategory()) != null) {
				throw new ApiException("The entered brand and category combination already exists\\nEnter a different brand or category");
			}
			pojo.setBrand(p.getBrand());
			pojo.setCategory(p.getCategory());
			brandDao.update(pojo);
	}
	}

	public void updateByCategory(String brand, BrandPojo p) throws ApiException {
		normalize(p);
		List<BrandPojo> list = getCheckByCategory(brand);
		for(BrandPojo pojo: list) {
			if(brandDao.selectByBrandAndCategory(p.getBrand(), p.getCategory()) != null) {
				throw new ApiException("The entered brand and category combination already exists\\nEnter a different brand or category");
			}
			pojo.setBrand(p.getBrand());
			pojo.setCategory(p.getCategory());
			brandDao.update(pojo);
		}
	}

	public BrandPojo getCheckById(int id) throws ApiException {
		BrandPojo p = brandDao.selectById(id);
		if (p == null) {
			throw new ApiException("Brand Category combination with given ID does not exit, id: " + id);
		}
		return p;
	}

	public List<BrandPojo> getCheckByBrand(String brand) throws ApiException {
		List<BrandPojo> p = brandDao.selectByBrand(brand);
		if (p == null) {
			throw new ApiException("Brand" + brand + "doesnot exist");
		}
		return p;
	}

	public List<BrandPojo> getCheckByCategory(String category) throws ApiException {
		List<BrandPojo> p = brandDao.selectByCategory(category);
		if (p == null) {
			throw new ApiException("category" + category + "doesnot exist");
		}
		return p;
	}

	public BrandPojo getCheckByBrandAndCategory(String brand, String category) throws ApiException {
		BrandPojo p = brandDao.selectByBrandAndCategory(brand, category);
		if (p == null) {
			throw new ApiException("Brand Category combination doesnot exist");
		}
		return p;
	}
	
	protected static void normalize(BrandPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
		p.setCategory(StringUtil.toLowerCase(p.getCategory()));
	}
}
