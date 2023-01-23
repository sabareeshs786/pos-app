package com.increff.posapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	private BrandDao brandDao;
	
	@Autowired
	private InventoryDao inventoryDao;
	
	@Autowired
	private InventoryService inventoryService;

	public void add(ProductPojo p) throws ApiException {
		normalize(p);
		if(StringUtil.isEmpty(p.getBarcode())) {
			throw new ApiException("Barcode cannot be empty");
		}
		if(StringUtil.isEmpty(Integer.toString(p.getBrandCategory()))){
			throw new ApiException("Brand Category combination id cannot be empty");
		}
		if(StringUtil.isEmpty(p.getName())) {
			throw new ApiException("Product name cannot be empty");
		}
		if(StringUtil.isEmpty(Double.toString(p.getMrp()))) {
			throw new ApiException("Product MRP cannot be empty");
		}
		if(p.getMrp() < 0) {
			throw new ApiException("MRP cannot be less than zero");
		}
		if(brandDao.selectById(p.getBrandCategory()) == null) {
			throw new ApiException("Product with specified brand and category doesn't exist");
		}

		//Inserting
		productDao.insert(p);
	}

	public void deleteById(int id) throws ApiException {
		inventoryDao.deleteById(id);
		productDao.deleteById(id);
	}

	public void deleteByBarcode(String barcode) {
		ProductPojo productPojo = productDao.selectByBarcode(barcode);
		inventoryDao.deleteByProductId(productPojo.getId());
		productDao.deleteByBarcode(barcode);
	}

	public void deleteByBrandCategory(Integer brand_category) {
		for(ProductPojo p: productDao.selectByBrandCategory(brand_category)) {
			inventoryDao.deleteByProductId(p.getId());
		}
		productDao.deleteByBrandCategory(brand_category);
	}

	public void deleteByName(String name) {
		inventoryDao.deleteById(productDao.selectByName(name).getId());
		productDao.deleteByName(name);
	}

	public void deleteByMrp(Double mrp) {
		for(ProductPojo p: productDao.selectByMrp(mrp)) {
			inventoryDao.deleteById(p.getId());
		}
		productDao.deleteByMrp(mrp);
	}

	public ProductPojo getById(int id) throws ApiException {
		System.out.println("ProductPojo getById");
		return getCheckById(id);
	}

	public ProductPojo getByBarcode(String barcode) throws ApiException {
		return getCheckByBarcode(barcode);
	}

	public List<ProductPojo> getByBrandCategory(Integer brand_category) throws ApiException {
		return getCheckByBrandCategory(brand_category);
	}

	public ProductPojo getByName(String name) throws ApiException {
		return getCheckByName(name);
	}

	public List<ProductPojo> getByMrp(Double mrp) throws ApiException {
		return getCheckByMrp(mrp);
	}

	public List<ProductPojo> getAll() {
		return productDao.selectAll();
	}

	public void updateById(int id, ProductPojo p) throws ApiException {
		normalize(p);
		ProductPojo ex = getCheckById(id);
		ex.setBarcode(p.getBarcode());
		ex.setBrandCategory(p.getBrandCategory());
		ex.setName(p.getName());
		ex.setMrp(p.getMrp());
		productDao.update(ex);
	}

	public void updateByBarcode(String barcode, ProductPojo p) throws ApiException {
		normalize(p);
		ProductPojo ex = getCheckByBarcode(barcode);
		ex.setBarcode(p.getBarcode());
		ex.setBrandCategory(p.getBrandCategory());
		ex.setName(p.getName());
		ex.setMrp(p.getMrp());
		productDao.update(ex);
	}

	public void updateByBrandCategory(Integer brandCategory, ProductPojo p) throws ApiException {
		normalize(p);
		List<ProductPojo> list = getCheckByBrandCategory(brandCategory);
		for(ProductPojo ex: list) {
		ex.setBarcode(p.getBarcode());
		ex.setBrandCategory(p.getBrandCategory());
		ex.setName(p.getName());
		ex.setMrp(p.getMrp());
		productDao.update(ex);
	}
	}

	public void updateByName(String name, ProductPojo p) throws ApiException {
		normalize(p);
		ProductPojo ex = getCheckByName(name);
		ex.setBarcode(p.getBarcode());
		ex.setBrandCategory(p.getBrandCategory());
		ex.setName(p.getName());
		ex.setMrp(p.getMrp());
		productDao.update(ex);
	}

	public void updateByMrp(Double mrp, ProductPojo p) throws ApiException {
		normalize(p);
		List<ProductPojo> list = getCheckByMrp(mrp);
		for(ProductPojo ex: list) {
		ex.setBarcode(p.getBarcode());
		ex.setBrandCategory(p.getBrandCategory());
		ex.setName(p.getName());
		ex.setMrp(p.getMrp());
		productDao.update(ex);
	}
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
		System.out.println("Barcode is "+p.getBarcode());
		if (p == null) {
			throw new ApiException("Item with given barcode "+ barcode + "doesnot exist");
		}
		return p;
	}

	public List<ProductPojo> getCheckByBrandCategory(Integer brand_category) throws ApiException {
		List<ProductPojo> p = productDao.selectByBrandCategory(brand_category);
		if (p == null) {
			throw new ApiException("Item with given brand-category "+ brand_category + "doesnot exist");
		}
		return p;
	}

	public ProductPojo getCheckByName(String name) throws ApiException {
		ProductPojo p = productDao.selectByName(name);
		if (p == null) {
			throw new ApiException("Item with given name "+ name + "doesnot exist");
		}
		return p;
	}

	public List<ProductPojo> getCheckByMrp(Double mrp) throws ApiException {
		List<ProductPojo> p = productDao.selectByMrp(mrp);
		if (p == null) {
			throw new ApiException("Item with given mrp "+ mrp + "doesnot exist");
		}
		return p;
	}
	
	protected static void normalize(ProductPojo p) {
		p.setBarcode(p.getBarcode().toLowerCase());
		p.setName(p.getName().toLowerCase());
		p.setMrp(DoubleUtil.round(p.getMrp(), 2));
	}


}
