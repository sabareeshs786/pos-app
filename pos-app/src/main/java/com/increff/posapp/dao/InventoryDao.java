package com.increff.posapp.dao;

import java.util.List;

import javax.persistence.Query;
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
	
	private static final String DELETE_BY_ID = "delete from InventoryPojo p where id=:id";
	private static final String DELETE_BY_PRODUCT_ID = "delete from InventoryPojo p where productId=:productId";
	private static final String DELETE_BY_QUANTITY = "delete from InventoryPojo p where quantity=:quantity";
	
	private static final String SELECT_BY_ID = "select p from InventoryPojo p where id=:id";
	private static final String SELECT_BY_PRODUCT_ID = "select p from InventoryPojo p where productId=:productId";
	private static final String SELECT_BY_QUANTITY = "select p from InventoryPojo p where quantity=:quantity";
	
	private static final String SELECT_ALL = "select p from InventoryPojo p";
	private static final String SELECT_ALL_COUNT = "select count(p) from InventoryPojo p";



	public void insert(InventoryPojo inventoryPojo) {
		em().persist(inventoryPojo);
	}

	public Integer deleteById(Integer id) {
		Query query = em().createQuery(DELETE_BY_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public Integer deleteByProductId(Integer pId) {
		Query query = em().createQuery(DELETE_BY_PRODUCT_ID);
		query.setParameter("productId",pId);
		return query.executeUpdate();
	}

	public Integer deleteByQuantity(Integer quantity) {
		Query query = em().createQuery(DELETE_BY_QUANTITY);
		query.setParameter("quantity", quantity);
		return query.executeUpdate();
	}
	
	public InventoryPojo selectById(Integer id) {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_BY_ID, InventoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public InventoryPojo selectByProductId(Integer pid) {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_BY_PRODUCT_ID, InventoryPojo.class);
		query.setParameter("productId", pid);
		return getSingle(query);
	}
	
	public List<InventoryPojo> selectByQuantity(Integer quantity) {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_BY_QUANTITY, InventoryPojo.class);
		query.setParameter("quantity", quantity);
		return query.getResultList();
	}
	
	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL, InventoryPojo.class);
		return query.getResultList();
	}

	public Page<InventoryPojo> getAllByPage(Integer page, Integer size){
		TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL, InventoryPojo.class);
		// private static String select_all = "select p from ProductPojo p";apply pagination
		int pageNumber = page;
		int pageSize = size;
		int firstResult = pageNumber * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);

		// execute the query
		List<InventoryPojo> entities = query.getResultList();

		Long totalElements = em().createQuery(SELECT_ALL_COUNT, Long.class).getSingleResult();
		return new PageImpl<>(entities, PageRequest.of(page, size), totalElements);
	}
	public void update(InventoryPojo inventoryPojo) {
		// Implemented by Spring itself
	}

}
