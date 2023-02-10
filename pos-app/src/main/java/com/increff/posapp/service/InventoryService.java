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

	public InventoryPojo add(InventoryPojo inventoryPojo) throws ApiException {
		validate(inventoryPojo);
		if (inventoryDao.selectByProductId(inventoryPojo.getProductId()) != null) {
			InventoryPojo p = inventoryDao.selectByProductId(inventoryPojo.getProductId());
			p.setQuantity(p.getQuantity() + inventoryPojo.getQuantity());
			return p;
		}
		return inventoryDao.add(inventoryPojo);
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

	public InventoryPojo updateByProductId(InventoryPojo inventoryPojo) throws ApiException {
		validate(inventoryPojo);
		InventoryPojo ex = getByProductId(inventoryPojo.getProductId());
		ex.setQuantity(inventoryPojo.getQuantity());
		inventoryDao.update(ex);
		return ex;
	}

	private InventoryPojo getCheckProductId(int productId) throws ApiException {
		InventoryPojo inventoryPojo = inventoryDao.selectByProductId(productId);
		if (inventoryPojo == null) {
			throw new ApiException("Item with given Product ID does not exist in the inventory");
		}
		return inventoryPojo;
	}

	private void validate(InventoryPojo p) throws ApiException {
		if(p.getProductId() == null ){
			throw new ApiException("Product id can't be empty");
		}
		if(p.getQuantity() == null ){
			throw new ApiException("Quantity can't be empty");
		}
		if(p.getQuantity() < 0){
			throw new ApiException("Quantity can't be less ta zero");
		}
	}
}
