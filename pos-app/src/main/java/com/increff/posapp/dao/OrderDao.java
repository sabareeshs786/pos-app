package com.increff.posapp.dao;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.posapp.service.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.increff.posapp.pojo.OrderPojo;

@Repository
@Transactional(rollbackOn = ApiException.class)
public class OrderDao extends AbstractDao{
	
	private static final String DELETE_BY_ID = "delete from OrderPojo p where id=:id";
	private static final String DELETE_BY_TIME = "delete from OrderPojo p where time=:time";
	private static final String SELECT_BY_ID = "select p from OrderPojo p where id=:id";
	private static final String SELECT_BY_TIME = "select p from OrderPojo p where time=:time";
	private static final String SELECT_BY_START_TIME = "select p from OrderPojo p where time >= :startTime";
	private static final String SELECT_BY_END_TIME = "select p from OrderPojo p where time <= :endTime";
	private static final String SELECT_BY_INTERVAL = "select p from OrderPojo p where time >= :startTime and time <=:endTime";
	private static final String SELECT_ALL = "select p from OrderPojo p";

	private static String SELECT_ALL_COUNT = "select count(p) from OrderPojo p";

	public void insert(OrderPojo p) {
		em().persist(p);
	}

	public Integer deleteById(Integer id) {
		Query query = em().createQuery(DELETE_BY_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public Integer deleteByTime(LocalDateTime time) {
		Query query = em().createQuery(DELETE_BY_TIME);
		query.setParameter("time",time);
		return query.executeUpdate();
	}

	public OrderPojo selectById(Integer id) {
		TypedQuery<OrderPojo> query = getQuery(SELECT_BY_ID, OrderPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<OrderPojo> selectByTime(ZonedDateTime time) {
		TypedQuery<OrderPojo> query = getQuery(SELECT_BY_TIME, OrderPojo.class);
		query.setParameter("time", time);
		return query.getResultList();
	}

	public List<OrderPojo> selectByStartTime(String startTime){
		TypedQuery<OrderPojo> query = getQuery(SELECT_BY_START_TIME, OrderPojo.class);
		query.setParameter("startTime", startTime);
		return query.getResultList();
	}
	public List<OrderPojo> selectByEndTime(String endTime){
		TypedQuery<OrderPojo> query = getQuery(SELECT_BY_END_TIME, OrderPojo.class);
		query.setParameter("endTime", endTime);
		return query.getResultList();
	}
	public List<OrderPojo> selectByInterval(ZonedDateTime startTime, ZonedDateTime endTime){
		TypedQuery<OrderPojo> query = getQuery(SELECT_BY_INTERVAL, OrderPojo.class);
		query.setParameter("startTime", startTime);
		query.setParameter("endTime", endTime);
		return query.getResultList();
	}
	public List<OrderPojo> selectAll() {
		TypedQuery<OrderPojo> query = getQuery(SELECT_ALL, OrderPojo.class);
		return query.getResultList();
	}

	public Page<OrderPojo> getAllByPage(Integer page, Integer size){
		TypedQuery<OrderPojo> query = getQuery(SELECT_ALL, OrderPojo.class);

		// private static String select_all = "select p from ProductPojo p";apply pagination
		int pageNumber = page;
		int pageSize = size;
		int firstResult = pageNumber * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);

		// execute the query
		List<OrderPojo> entities = query.getResultList();
		Long totalElements = em().createQuery(SELECT_ALL_COUNT, Long.class).getSingleResult();
		return new PageImpl<>(entities, PageRequest.of(page, size), totalElements);
	}
	public void update(OrderPojo p) {
		// Implemented by Spring itself
	}
}
