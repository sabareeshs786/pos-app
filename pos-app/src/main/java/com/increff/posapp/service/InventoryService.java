package com.increff.posapp.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.increff.posapp.dao.InventoryDao;
import com.increff.posapp.pojo.InventoryPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {

	@Autowired
	private InventoryDao inventoryDao;

	public void add(InventoryPojo inventoryPojo) throws ApiException {

		if (inventoryDao.selectByProductId(inventoryPojo.getProductId()) != null) {
			InventoryPojo p = inventoryDao.selectByProductId(inventoryPojo.getProductId());
			Integer q = p.getQuantity();
			p.setQuantity(q + inventoryPojo.getQuantity());
			return;
		}
		inventoryDao.insert(inventoryPojo);
	}

	public InventoryPojo getById(int id) throws ApiException {
		return getCheckId(id);
	}

	  
	public InventoryPojo getByProductId(int pid) throws ApiException {
		return getCheckProductId(pid);
	}

	public List<InventoryPojo> getAll() {
		return inventoryDao.selectAll();
	}
	public Page<InventoryPojo> getAllByPage(Integer page, Integer size){
		return inventoryDao.getAllByPage(page, size);
	}
	 
	public void updateById(int id, InventoryPojo inventoryPojo) throws ApiException {
		InventoryPojo ex = getById(id);
		ex.setProductId(inventoryPojo.getProductId());
		ex.setQuantity(inventoryPojo.getQuantity());
		inventoryDao.update(ex);
	}

	 
	public void updateByProductId(Integer pid, InventoryPojo inventoryPojo) throws ApiException {
		InventoryPojo ex = getByProductId(pid);
		ex.setQuantity(inventoryPojo.getQuantity());
		inventoryDao.update(ex);
	}

	private InventoryPojo getCheckId(int id) throws ApiException {
		InventoryPojo inventoryPojo = inventoryDao.selectById(id);
		if (inventoryPojo == null) {
			throw new ApiException("Item with given ID does not exit in the inventory");
		}
		return inventoryPojo;
	}

	private InventoryPojo getCheckProductId(int productId) throws ApiException {
		InventoryPojo inventoryPojo = inventoryDao.selectByProductId(productId);
		if (inventoryPojo == null) {
			throw new ApiException("Item with given Product ID does not exist in the inventory");
		}
		return inventoryPojo;
	}
}
