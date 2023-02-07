package com.increff.posapp.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.posapp.pojo.BrandPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.increff.posapp.dao.BrandDao;
import com.increff.posapp.dao.InventoryDao;
import com.increff.posapp.dao.ProductDao;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.util.DoubleUtil;
import com.increff.posapp.util.StringUtil;

@Service
@Transactional(rollbackOn = ApiException.class)
public class ProductService {

	@Autowired
	private ProductDao productDao;

	public void add(ProductPojo p) throws ApiException {
		normalize(p);
		validate(p);
		//Inserting
		productDao.insert(p);
	}

	public ProductPojo getById(int id) throws ApiException {
		return getCheckById(id);
	}

	public ProductPojo getByBarcode(String barcode) throws ApiException {
		return getCheckByBarcode(barcode);
	}

	public Page<ProductPojo> getByBrandCategory(Integer brand_category, Integer page, Integer size) throws ApiException {
		return getCheckByBrandCategory(brand_category, page, size);
	}

	public ProductPojo getByName(String name) throws ApiException {
		name = name.toLowerCase();
		return getCheckByName(name);
	}

	public List<ProductPojo> getByMrp(Double mrp) throws ApiException {
		return getCheckByMrp(mrp);
	}

	public List<ProductPojo> getAll() {
		return productDao.selectAll();
	}

	public Page<ProductPojo> getAllByPage(Integer page, Integer size){
		return productDao.getAllByPage(page, size);
	}
	public void updateById(int id, ProductPojo p) throws ApiException {
		normalize(p);
		validate(p);
		ProductPojo ex = getCheckById(id);
		ex.setBarcode(p.getBarcode());
		ex.setBrandCategory(p.getBrandCategory());
		ex.setName(p.getName());
		ex.setMrp(p.getMrp());
		productDao.update(ex);
	}

	public ProductPojo getCheckById(Integer id) throws ApiException {
		ProductPojo p = productDao.selectById(id);
		if (p == null) {
			throw new ApiException("Product with given ID does not exist");
		}
		return p;
	}

	public ProductPojo getCheckByBarcode(String barcode) throws ApiException {
		ProductPojo p = productDao.selectByBarcode(barcode);
		if (p == null) {
			throw new ApiException("Product with given barcode doesn't exist");
		}
		return p;
	}

	public Page<ProductPojo> getCheckByBrandCategory(Integer brandCategory, Integer page, Integer size) throws ApiException {
		Page<ProductPojo> p = productDao.selectByBrandCategory(brandCategory, page, size);
		if (p.getTotalElements() == 0) {
			throw new ApiException("Item with given brand-category doesn't exist");
		}
		return p;
	}

	public ProductPojo getCheckByName(String name) throws ApiException {
		ProductPojo p = productDao.selectByName(name);
		if (p == null) {
			throw new ApiException("Item with given name doesn't exist");
		}
		return p;
	}

	public List<ProductPojo> getCheckByMrp(Double mrp) throws ApiException {
		List<ProductPojo> p = productDao.selectByMrp(mrp);
		if (p == null) {
			throw new ApiException("Item with given mrp doesn't exist");
		}
		return p;
	}
	
	protected static void normalize(ProductPojo p) {
		p.setBarcode(p.getBarcode().toLowerCase());
		p.setName(p.getName().toLowerCase());
		p.setMrp(DoubleUtil.round(p.getMrp(), 2));
	}

	protected void validate(ProductPojo p) throws ApiException {
		if(p.getName().isEmpty()){
			throw new ApiException("Product name can't be empty");
		}
		if(p.getBarcode().isEmpty()){
			throw new ApiException("Barcode can't be empty");
		}
		if(p.getBrandCategory().toString().isEmpty()){
			throw new ApiException("Brand-Category number can't be empty");
		}
		if(p.getMrp().toString().isEmpty()){
			throw new ApiException("MRP can't be empty");
		}
		if(p.getMrp().isInfinite() || p.getMrp().isNaN()) {
			throw new ApiException("MRP is invalid");
		}
		if(p.getBrandCategory() <= 0){
			throw new ApiException("Brand-Category must be greater than zero");
		}
		if(p.getMrp() <= 0.0){
			throw new ApiException("MRP must be greater than zero");
		}
	}
}