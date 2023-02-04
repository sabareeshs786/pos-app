package com.increff.posapp.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.increff.posapp.service.ApiException;
import org.springframework.stereotype.Repository;
import com.increff.posapp.pojo.BrandPojo;

@Repository
@Transactional(rollbackOn = ApiException.class)
public class BrandDao extends AbstractDao {

	private static final Logger logger = Logger.getLogger(BrandDao.class);
	// For deleting
	private static final String DELETE_BY_ID = "delete from BrandPojo p where id=:id";
	private static final String DELETE_BY_BRAND = "delete from BrandPojo p where brand=:brand";
	private static final String DELETE_BY_CATEGORY = "delete from BrandPojo p where category=:category";
	
	private static final String SELECT_BY_ID = "select p from BrandPojo p where id=:id";
	private static final String SELECT_BY_BRAND = "select p from BrandPojo p where brand=:brand";
	private static final String SELECT_BY_CATEGORY = "select p from BrandPojo p where category=:category";
	private static final String SELECT_BY_BRAND_AND_CATEGORY = "select p from BrandPojo p where brand=:brand and category=:category";
	private static final String SELECT_ALL = "select p from BrandPojo p";
	private static final String SELECT_ALL_COUNT = "select count(p) from BrandPojo p";
	private static final String SELECT_BY_BRAND_COUNT = "select count(p) from BrandPojo p where brand=:brand";
	private static final String SELECT_BY_CATEGORY_COUNT = "select count(p) from BrandPojo p where category=:category";
	
	public void insert(BrandPojo p) {
		em().persist(p);
	}

	public Integer deleteById(Integer id) {
		Query query = em().createQuery(DELETE_BY_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public Integer deleteByBrand(String brand) {
		Query query = em().createQuery(DELETE_BY_BRAND);
		query.setParameter("brand", brand);
		return query.executeUpdate();
	}
	
	public Integer deleteByCategory(String category) {
		Query query = em().createQuery(DELETE_BY_CATEGORY);
		query.setParameter("category", category);
		return query.executeUpdate();
	}
	
	public BrandPojo selectById(Integer id) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_BY_ID, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public Page<BrandPojo> selectByBrand(String brand, Integer page, Integer size) {

		TypedQuery<BrandPojo> query = getQuery(SELECT_BY_BRAND, BrandPojo.class);
		query.setParameter("brand", brand);

		logger.info("From BrandDao  brand="+brand);
		int pageNumber = page;
		int pageSize = size;
		int firstResult = pageNumber * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);

		List<BrandPojo> entities = query.getResultList();
		Long totalElements = em().createQuery(SELECT_BY_BRAND_COUNT, Long.class).setParameter("brand", brand).getSingleResult();
		return new PageImpl<>(entities, PageRequest.of(page, size), totalElements);
	}
	
	public Page<BrandPojo> selectByCategory(String category, Integer page, Integer size) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_BY_CATEGORY, BrandPojo.class);
		query.setParameter("category", category);

		int pageNumber = page;
		int pageSize = size;
		int firstResult = pageNumber * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);

		List<BrandPojo> entities = query.getResultList();
		Long totalElements = em().createQuery(SELECT_BY_CATEGORY_COUNT, Long.class).setParameter("category", category).getSingleResult();
		return new PageImpl<>(entities, PageRequest.of(page, size), totalElements);
	}
	
	public BrandPojo selectByBrandAndCategory(String brand, String category) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_BY_BRAND_AND_CATEGORY, BrandPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}
	
	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
		return query.getResultList();
	}
	
	public Page<BrandPojo> getAllByPage(Integer page, Integer size){
		TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
		// private static String select_all = "select p from ProductPojo p";apply pagination
        int pageNumber = page;
        int pageSize = size;
        int firstResult = pageNumber * pageSize;
        query.setFirstResult(firstResult);
        query.setMaxResults(pageSize);

        // execute the query
        List<BrandPojo> entities = query.getResultList();
        Long totalElements = em().createQuery(SELECT_ALL_COUNT, Long.class).getSingleResult();
        return new PageImpl<>(entities, PageRequest.of(page, size), totalElements);
	}

	public void update(BrandPojo p) {
		// Handled by Spring itself
	}

}
