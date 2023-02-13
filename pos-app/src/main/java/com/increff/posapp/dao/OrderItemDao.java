package com.increff.posapp.dao;

import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.service.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional(rollbackOn = ApiException.class)
public class OrderItemDao extends AbstractDao{

	private static final String SELECT_BY_ID = "select p from OrderItemPojo p where id=:id";
	private static final String SELECT_BY_ORDER_ID = "select p from OrderItemPojo p where orderId=:orderId";
	private static final String SELECT_ALL = "select p from OrderItemPojo p";
	private static final String SUM_OF_INVOICED_QUANTITY_BY_ORDER_ID = "select sum(quantity) from OrderItemPojo p where orderId=:orderId";
	private static final String TOTAL_COST_BY_ORDER_ID = "select sum(quantity*sellingPrice) from OrderItemPojo p where orderId=:orderId";
	private static final String SELECT_BY_ORDER_ID_COUNT = "select count(p) from OrderItemPojo p where orderId=:orderId";

	public OrderItemPojo insert(OrderItemPojo p) {
		em().persist(p);
		return p;
	}

	public OrderItemPojo selectById(Integer id) {
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ID, OrderItemPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<OrderItemPojo> selectByOrderId(Integer orderId) {
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ORDER_ID, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}

	public Page<OrderItemPojo> getPageByOrderId(Integer orderId, Integer page, Integer size) {
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ORDER_ID, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		int pageNumber = page;
		int pageSize = size;
		int firstResult = pageNumber * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);

		// execute the query
		List<OrderItemPojo> entities = query.getResultList();
		Long totalElements = em().createQuery(SELECT_BY_ORDER_ID_COUNT, Long.class).setParameter("orderId", orderId).getSingleResult();
		return new PageImpl<>(entities, PageRequest.of(page, size), totalElements);
	}

	public List<OrderItemPojo> selectAll() {
		TypedQuery<OrderItemPojo> query = getQuery(SELECT_ALL, OrderItemPojo.class);
		return query.getResultList();
	}

	public Long getInvoicedQuantityByOrderId(Integer orderId){
		return em().createQuery(SUM_OF_INVOICED_QUANTITY_BY_ORDER_ID, Long.class).setParameter("orderId", orderId).getSingleResult();
	}

	public Double getTotalCostByOrderId(Integer orderId){
		return em().createQuery(TOTAL_COST_BY_ORDER_ID, Double.class).setParameter("orderId", orderId).getSingleResult();
	}

	public void update(OrderItemPojo p) {
		// Implemented by Spring itself
	}

}
