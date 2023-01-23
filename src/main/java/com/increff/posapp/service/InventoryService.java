package com.increff.posapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.posapp.dao.InventoryDao;
import com.increff.posapp.dao.ProductDao;
import com.increff.posapp.pojo.InventoryPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {

	@Autowired
	private InventoryDao inventoryDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductService productService;

	 
	public void add(InventoryPojo inventoryPojo) throws ApiException {

		if (productDao.selectById(inventoryPojo.getProductId()) == null) {
			throw new ApiException("Product doesn't exist");
		}

		if (inventoryDao.selectByPId(inventoryPojo.getProductId()) != null) {
			InventoryPojo p = inventoryDao.selectByPId(inventoryPojo.getProductId());
			Integer q = p.getQuantity();
			p.setQuantity(q + inventoryPojo.getQuantity());
			return;
		}

		inventoryDao.insert(inventoryPojo);
	}

	// For deletion
	  
	public void deleteById(int id) throws ApiException {
		getCheckId(id);
		inventoryDao.deleteById(id);
	}

	  
	public void deleteByProductId(Integer pId) throws ApiException {
		productService.getCheckById(pId);
		getCheckPId(pId);
		inventoryDao.deleteByProductId(pId);
	}

	  
	public void deleteByQuantity(Integer quantity) {
		inventoryDao.deleteByQuantity(quantity);
	}

	// For getting data
	 
	public InventoryPojo getById(int id) throws ApiException {
		return getCheckId(id);
	}

	  
	public InventoryPojo getByPid(int pid) throws ApiException {
		return getCheckPId(pid);
	}

	 
	public List<InventoryPojo> getByQuantity(Integer quantity) throws ApiException {
		return getCheckQuantity(quantity);
	}

	  
	public List<InventoryPojo> getAll() {
		return inventoryDao.selectAll();
	}

	 
	public void updateById(int id, InventoryPojo inventoryPojo) throws ApiException {
		InventoryPojo ex = getById(id);
		ex.setProductId(inventoryPojo.getProductId());
		ex.setQuantity(inventoryPojo.getQuantity());
		inventoryDao.update(ex);
	}

	 
	public void updateByPid(Integer pid, InventoryPojo inventoryPojo) throws ApiException {
		InventoryPojo ex = getByPid(pid);
		ex.setQuantity(inventoryPojo.getQuantity());
		inventoryDao.update(ex);
	}

	 
	public void updateByQuantity(Integer quantity, InventoryPojo inventoryPojo) throws ApiException {
		List<InventoryPojo> list = getByQuantity(quantity);
		for (InventoryPojo ex : list) {
			ex.setProductId(inventoryPojo.getProductId());
			ex.setQuantity(inventoryPojo.getQuantity());
			inventoryDao.update(ex);
		}
	}

	  
	public InventoryPojo getCheckId(int id) throws ApiException {
		InventoryPojo inventoryPojo = inventoryDao.selectById(id);
		if (inventoryPojo == null) {
			throw new ApiException("Item with given ID does not exit in the inventory");
		}
		return inventoryPojo;
	}

	  
	private InventoryPojo getCheckPId(int pid) throws ApiException {
		InventoryPojo inventoryPojo = inventoryDao.selectByPId(pid);
		if (inventoryPojo == null) {
			throw new ApiException("Item with given Product ID does not exist in the inventory");
		}
		return inventoryPojo;
	}

	  
	public List<InventoryPojo> getCheckQuantity(Integer quantity) throws ApiException {
		List<InventoryPojo> list = inventoryDao.selectByQuantity(quantity);
		if (list.isEmpty()) {
			throw new ApiException("Item with given quantity does not exist in the inventory");
		}
		return list;
	}

	private void validate(){
	}
}
