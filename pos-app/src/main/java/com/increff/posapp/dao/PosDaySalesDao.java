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

	private static final String SELECT_BY_INTERVAL = "select p from PosDaySalesPojo p where date >= :startDate and date <= :endDate";
	private static final String SELECT_LAST_DATE_TIME = "select max(date) from PosDaySalesPojo p";

	public void insert(PosDaySalesPojo p) {
		em().persist(p);
	}

	public List<PosDaySalesPojo> selectByInterval(ZonedDateTime startDate, ZonedDateTime endDate){
		TypedQuery<PosDaySalesPojo> query = getQuery(SELECT_BY_INTERVAL, PosDaySalesPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	public ZonedDateTime getLastDateTime(){
		return em().createQuery(SELECT_LAST_DATE_TIME, ZonedDateTime.class).getSingleResult();
	}

}
