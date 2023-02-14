package com.increff.posapp.dao;

import com.increff.posapp.pojo.PosDaySalesPojo;
import com.increff.posapp.service.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
@Transactional(rollbackOn = ApiException.class)
public class PosDaySalesDao extends AbstractDao{

	private static final String SELECT_BY_DATE = "select p from PosDaySalesPojo p where date=:date";
	private static final String SELECT_BY_START_TIME = "select p from PosDaySalesPojo p where date >= :startDate";
	private static final String SELECT_BY_END_TIME = "select p from PosDaySalesPojo p where date <= :endDate";
	private static final String SELECT_BY_INTERVAL = "select p from PosDaySalesPojo p where date >= :startDate and date <=:endDate";
	private static final String SELECT_ALL = "select p from PosDaySalesPojo p";
	private static final String SELECT_ALL_COUNT = "select count(p) from PosDaySalesPojo p";
	private static final String SELECT_LAST_DATE_TIME = "select max(zonedDateTime) from PosDaySalesPojo p";

	public void insert(PosDaySalesPojo p) {
		em().persist(p);
	}

	public List<PosDaySalesPojo> selectByDate(ZonedDateTime date) {
		TypedQuery<PosDaySalesPojo> query = getQuery(SELECT_BY_DATE, PosDaySalesPojo.class);
		query.setParameter("date", date);
		return query.getResultList();
	}

	public List<PosDaySalesPojo> selectByStartDate(ZonedDateTime startDate){
		TypedQuery<PosDaySalesPojo> query = getQuery(SELECT_BY_START_TIME, PosDaySalesPojo.class);
		query.setParameter("startDate", startDate);
		return query.getResultList();
	}
	public List<PosDaySalesPojo> selectByEndDate(ZonedDateTime endDate){
		TypedQuery<PosDaySalesPojo> query = getQuery(SELECT_BY_END_TIME, PosDaySalesPojo.class);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}
	public List<PosDaySalesPojo> selectByInterval(ZonedDateTime startDate, ZonedDateTime endDate){
		TypedQuery<PosDaySalesPojo> query = getQuery(SELECT_BY_INTERVAL, PosDaySalesPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}
	public List<PosDaySalesPojo> selectAll() {
		TypedQuery<PosDaySalesPojo> query = getQuery(SELECT_ALL, PosDaySalesPojo.class);
		return query.getResultList();
	}

	public Page<PosDaySalesPojo> getAllByPage(Integer page, Integer size){
		TypedQuery<PosDaySalesPojo> query = getQuery(SELECT_ALL, PosDaySalesPojo.class);

		int pageNumber = page;
		int pageSize = size;
		int firstResult = pageNumber * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);

		List<PosDaySalesPojo> entities = query.getResultList();
		Long totalElements = em().createQuery(SELECT_ALL_COUNT, Long.class).getSingleResult();
		return new PageImpl<>(entities, PageRequest.of(page, size), totalElements);
	}

	public ZonedDateTime getLastDateTime(){
		return em().createQuery(SELECT_LAST_DATE_TIME, ZonedDateTime.class).getSingleResult();
	}

}
