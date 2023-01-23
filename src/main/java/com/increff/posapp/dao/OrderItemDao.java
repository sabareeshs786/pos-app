package com.increff.posapp.dao;

import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.service.ApiException;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional(rollbackOn = ApiException.class)
public class OrderItemDao extends AbstractDao{

	private static String delete_id = "delete from OrderItemPojo p where id=:id";
	private static String delete_orderId = "delete from OrderItemPojo p where orderId=:orderId";
	private static String delete_productId = "delete from OrderItemPojo p where productId=:productId";
	private static String delete_quantity = "delete from OrderItemPojo p where quantity=:quantity";
	private static String delete_selling_price = "delete from OrderItemPojo p where sellingPrice=:sellingPrice";

	private static String select_id = "select p from OrderItemPojo p where id=:id";
	private static String select_orderId = "select p from OrderItemPojo p where orderId=:orderId";
	private static String select_productId = "select p from OrderItemPojo p where productId=:productId";
	private static String select_quantity = "select p from OrderItemPojo p where quantity=:quantity";
	private static String select_selling_price = "select p from OrderItemPojo p where sellingPrice=:sellingPrice";

	private static String select_all = "select p from OrderItemPojo p";

	public void insert(OrderItemPojo p) {
		em().persist(p);
	}

	public Integer deleteById(Integer id) {
		Query query = em().createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public Integer deleteByOrderId(Integer orderId) {
		Query query = em().createQuery(delete_orderId);
		query.setParameter("orderId", orderId);
		return query.executeUpdate();
	}

	public Integer deleteByProductId(Integer productId) {
		Query query = em().createQuery(delete_productId);
		query.setParameter("productId", productId);
		return query.executeUpdate();
	}

	public Integer deleteByQuantity(Integer quantity) {
		Query query = em().createQuery(delete_quantity);
		query.setParameter("quantity", quantity);
		return query.executeUpdate();
	}

	public Integer deleteBySellingPrice(Double sellingPrice) {
		Query query = em().createQuery(delete_selling_price);
		query.setParameter("sellingPrice", sellingPrice);
		return query.executeUpdate();
	}

	public OrderItemPojo selectById(Integer id) {
		TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<OrderItemPojo> selectByOrderId(Integer orderId) {
		TypedQuery<OrderItemPojo> query = getQuery(select_orderId, OrderItemPojo.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}

	public OrderItemPojo selectByProductId(Integer productId) {
		TypedQuery<OrderItemPojo> query = getQuery(select_productId, OrderItemPojo.class);
		query.setParameter("productId", productId);
		return getSingle(query);
	}

	public List<OrderItemPojo> selectByQuantity(Integer quantity) {
		TypedQuery<OrderItemPojo> query = getQuery(select_quantity, OrderItemPojo.class);
		query.setParameter("quantity", quantity);
		return query.getResultList();
	}

	public List<OrderItemPojo> selectBySellingPrice(Double sellingPrice) {
		TypedQuery<OrderItemPojo> query = getQuery(select_selling_price, OrderItemPojo.class);
		query.setParameter("sellingPrice", sellingPrice);
		return query.getResultList();
	}

	public List<OrderItemPojo> selectAll() {
		TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
		return query.getResultList();
	}

	public void update(OrderItemPojo p) {
		// Implemented by Spring itself
	}

}
