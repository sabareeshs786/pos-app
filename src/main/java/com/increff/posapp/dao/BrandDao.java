package com.increff.posapp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.increff.posapp.service.ApiException;
import org.springframework.stereotype.Repository;
import com.increff.posapp.pojo.BrandPojo;

@Repository
@Transactional(rollbackOn = ApiException.class)
public class BrandDao extends AbstractDao {
	
	// For deleting
	private static String delete_id = "delete from BrandPojo p where id=:id";
	private static String delete_brand = "delete from BrandPojo p where brand=:brand";
	private static String delete_category = "delete from BrandPojo p where category=:category";
	
	private static String select_id = "select p from BrandPojo p where id=:id";
	private static String select_brand = "select p from BrandPojo p where brand=:brand";
	private static String select_category = "select p from BrandPojo p where category=:category";
	private static String select_brand_category = "select p from BrandPojo p where brand=:brand and category=:category";
	private static String select_all = "select p from BrandPojo p";
	private static String select_all_count = "select count(p) from BrandPojo p";
	
	public void insert(BrandPojo p) {
		em().persist(p);
	}

	public Integer deleteById(Integer id) {
		Query query = em().createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	
	public Integer deleteByBrand(String brand) {
		Query query = em().createQuery(delete_brand);
		query.setParameter("brand", brand);
		return query.executeUpdate();
	}
	
	public Integer deleteByCategory(String category) {
		Query query = em().createQuery(delete_category);
		query.setParameter("category", category);
		return query.executeUpdate();
	}
	
	public BrandPojo selectById(Integer id) {
		TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public List<BrandPojo> selectByBrand(String brand) {
		TypedQuery<BrandPojo> query = getQuery(select_brand, BrandPojo.class);
		query.setParameter("brand", brand);
		return query.getResultList();
	}
	
	public List<BrandPojo> selectByCategory(String category) {
		TypedQuery<BrandPojo> query = getQuery(select_category, BrandPojo.class);
		query.setParameter("category", category);
		return query.getResultList();
	}
	
	public BrandPojo selectByBrandAndCategory(String brand, String category) {
		TypedQuery<BrandPojo> query = getQuery(select_brand_category, BrandPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);
		return getSingle(query);
	}
	
	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
		return query.getResultList();
	}
	
	public Page<BrandPojo> getAllByPage(Integer page, Integer size){
		TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
		// apply pagination
        int pageNumber = page;
        int pageSize = size;
        int firstResult = pageNumber * pageSize;
        query.setFirstResult(firstResult);
        query.setMaxResults(pageSize);

        // execute the query
        List<BrandPojo> entities = query.getResultList();
        Long totalElements = em().createQuery(select_all_count, Long.class).getSingleResult();
        return new PageImpl<>(entities, PageRequest.of(page, size), totalElements);
	}

	public void update(BrandPojo p) {
		// Handled by Spring itself
	}

}
