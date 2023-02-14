package com.increff.posapp.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.service.ApiException;

@Repository
@Transactional(rollbackOn = ApiException.class)
public class InventoryDao extends AbstractDao{

	private static final String SELECT_BY_PRODUCT_ID = "select p from InventoryPojo p where productId=:productId";
	private static final String SELECT_BY_QUANTITY = "select p from InventoryPojo p where quantity=:quantity";
	
	private static final String SELECT_ALL = "select p from InventoryPojo p";
	private static final String SELECT_ALL_COUNT = "select count(p) from InventoryPojo p";



	public InventoryPojo add(InventoryPojo inventoryPojo) {
		em().persist(inventoryPojo);
		return inventoryPojo;
	}

	public InventoryPojo selectByProductId(Integer pid) {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_BY_PRODUCT_ID, InventoryPojo.class);
		query.setParameter("productId", pid);
		return getSingle(query);
	}

	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL, InventoryPojo.class);
		return query.getResultList();
	}

	public Page<InventoryPojo> getAllByPage(Integer page, Integer size){
		TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL, InventoryPojo.class);
		int pageNumber = page;
		int pageSize = size;
		int firstResult = pageNumber * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);

		List<InventoryPojo> entities = query.getResultList();

		Long totalElements = em().createQuery(SELECT_ALL_COUNT, Long.class).getSingleResult();
		return new PageImpl<>(entities, PageRequest.of(page, size), totalElements);
	}
	public void update(InventoryPojo inventoryPojo) {
		// Implemented by Spring itself
	}

}
