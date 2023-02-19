package com.increff.posapp.dao;

import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.service.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

public abstract class AbstractDao {
	
	@PersistenceContext
	private EntityManager em;

	private <T> String getSelectAllQueryString(Class<T> clazz){
		return "select p from " + clazz.getName() + " p ";
	}

	private <T> String getSelectAllCount(Class<T> tClass){
		return "select count(p) from " + tClass.getName() + " p ";
	}
	protected <T> T getSingle(TypedQuery<T> query) {
		return query.getResultList().stream().findFirst().orElse(null);
	}
	
	protected <T> TypedQuery<T> getQuery(String jpql, Class<T> clazz) {
		return em.createQuery(jpql, clazz);
	}
	
	protected EntityManager em() {
		return em;
	}

	// General methods
	@Transactional(rollbackOn = ApiException.class)
	public <T> Object insert(Object pojo){
		em.persist(pojo);
		return pojo;
	}

	@Transactional(rollbackOn = ApiException.class)
	public <T> List<T> selectAll(Class<T> clazz){
		TypedQuery<T> query = getQuery(getSelectAllQueryString(clazz), clazz);
		return query.getResultList();
	}

	@Transactional(rollbackOn = ApiException.class)
	public <T> Page<T> selectAllByPage(Class<T> tClass, Integer page, Integer size){
		TypedQuery<T> query = getQuery(getSelectAllQueryString(tClass), tClass);
		query.setFirstResult(page*size);
		query.setMaxResults(size);

		List<T> entities = query.getResultList();
		Long totalElements = em().createQuery(getSelectAllCount(tClass), Long.class).getSingleResult();
		return new PageImpl<>(entities, PageRequest.of(page, size), totalElements);
	}

}
