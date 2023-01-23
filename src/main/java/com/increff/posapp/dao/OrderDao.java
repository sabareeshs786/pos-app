package com.increff.posapp.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.posapp.service.ApiException;
import org.springframework.stereotype.Repository;

import com.increff.posapp.pojo.OrderPojo;

@Repository
@Transactional(rollbackOn = ApiException.class)
public class OrderDao extends AbstractDao{
	
	private static String delete_id = "delete from OrderPojo p where id=:id";
	private static String delete_time = "delete from OrderPojo p where time=:time";
	private static String select_id = "select p from OrderPojo p where id=:id";
	private static String select_time = "select p from OrderPojo p where time=:time";
	private static String select_all = "select p from OrderPojo p";

	public void insert(OrderPojo p) {
		em().persist(p);
	}

	public Integer deleteById(Integer id) {
		Query query = em().createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public Integer deleteByTime(LocalDateTime time) {
		Query query = em().createQuery(delete_time);
		query.setParameter("time",time);
		return query.executeUpdate();
	}

	public OrderPojo selectById(Integer id) {
		TypedQuery<OrderPojo> query = getQuery(select_id, OrderPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<OrderPojo> selectByTime(LocalDateTime time) {
		TypedQuery<OrderPojo> query = getQuery(select_time, OrderPojo.class);
		query.setParameter("time", time);
		return query.getResultList();
	}
	
	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
		return query.getResultList();
	}

	public void update(OrderPojo p) {
		// Implemented by Spring itself
	}
}
