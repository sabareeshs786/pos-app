package com.increff.posapp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.service.ApiException;

@Repository
@Transactional(rollbackOn = ApiException.class)
public class InventoryDao extends AbstractDao{
	
	private static String delete_id = "delete from InventoryPojo p where id=:id";
	private static String delete_productId = "delete from InventoryPojo p where productId=:productId";
	private static String delete_quantity = "delete from InventoryPojo p where quantity=:quantity";
	
	private static String select_id = "select p from InventoryPojo p where id=:id";
	private static String select_productId = "select p from InventoryPojo p where productId=:productId";
	private static String select_quantity = "select p from InventoryPojo p where quantity=:quantity";
	
	private static String select_all = "select p from InventoryPojo p";



	public void insert(InventoryPojo inventoryPojo) {
		em().persist(inventoryPojo);
	}

	public Integer deleteById(Integer id) {
		Query query = em().createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public Integer deleteByProductId(Integer pId) {
		Query query = em().createQuery(delete_productId);
		query.setParameter("productId",pId);
		return query.executeUpdate();
	}

	public Integer deleteByQuantity(Integer quantity) {
		Query query = em().createQuery(delete_quantity);
		query.setParameter("quantity", quantity);
		return query.executeUpdate();
	}
	
	public InventoryPojo selectById(Integer id) {
		TypedQuery<InventoryPojo> query = getQuery(select_id, InventoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public InventoryPojo selectByPId(Integer pid) {
		TypedQuery<InventoryPojo> query = getQuery(select_productId, InventoryPojo.class);
		query.setParameter("productId", pid);
		return getSingle(query);
	}
	
	public List<InventoryPojo> selectByQuantity(Integer quantity) {
		TypedQuery<InventoryPojo> query = getQuery(select_quantity, InventoryPojo.class);
		query.setParameter("quantity", quantity);
		return query.getResultList();
	}
	
	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(select_all, InventoryPojo.class);
		return query.getResultList();
	}

	public void update(InventoryPojo inventoryPojo) {
		// Implemented by Spring itself
	}

}
