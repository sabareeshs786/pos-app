package com.increff.posapp.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.posapp.service.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.increff.posapp.pojo.ProductPojo;

@Repository
@Transactional(rollbackOn = ApiException.class)
public class ProductDao extends AbstractDao{
	
	private static final String DELETE_BY_ID = "delete from ProductPojo p where id=:id";
	private static final String DELETE_BY_BARCODE = "delete from ProductPojo p where barcode=:barcode";
	private static final String DELETE_BY_BRAND_AND_CATEGORY = "delete from ProductPojo p where brand_category=:brand_category";
	private static final String DELETE_BY_NAME = "delete from ProductPojo p where name=:name";
	private static final String DELETE_BY_MRP = "delete from ProductPojo p where mrp=:mrp";
	
	private static final String SELECT_BY_ID = "select p from ProductPojo p where id=:id";
	private static final String SELECT_BY_ID_COUNT = "select count(p) from ProductPojo p where id=:id";
	private static final String SELECT_BY_BARCODE = "select p from ProductPojo p where barcode=:barcode";
	private static final String SELECT_BY_BRAND_CATEGORY = "select p from ProductPojo p where brand_category=:brand_category";
	private static final String SELECT_BY_NAME = "select p from ProductPojo p where name=:name";
	private static final String SELECT_BY_MRP = "select p from ProductPojo p where mrp=:mrp";
	private static final String SELECT_ALL = "select p from ProductPojo p";
	private static final String SELECT_ALL_COUNT = "select count(p) from ProductPojo p";
	private static final String SELECT_BY_BRAND_CATEGORY_COUNT = "select count(p) from ProductPojo p where brand_category=:brandCategory";


	@Transactional
	public ProductPojo insert(ProductPojo productPojo) {
		em().persist(productPojo);
		return productPojo;
	}

	public Integer deleteById(Integer id) {
		Query query = em().createQuery(DELETE_BY_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	public Integer deleteByBarcode(String barcode) {
		Query query = em().createQuery(DELETE_BY_BARCODE);
		query.setParameter("barcode", barcode);
		return query.executeUpdate();
	}
	
	public Integer deleteByBrandCategory(Integer brand_category) {
		Query query = em().createQuery(DELETE_BY_BRAND_AND_CATEGORY);
		query.setParameter("brand_category", brand_category);
		return query.executeUpdate();
	}
	
	public Integer deleteByName(String name) {
		Query query = em().createQuery(DELETE_BY_NAME);
		query.setParameter("name", name);
		return query.executeUpdate();
	}
	
	public Integer deleteByMrp(Double mrp) {
		Query query = em().createQuery(DELETE_BY_MRP);
		query.setParameter("mrp", mrp);
		return query.executeUpdate();
	}
	
	public ProductPojo selectById(Integer id) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_BY_ID, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public ProductPojo selectByBarcode(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_BY_BARCODE, ProductPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}
	
	public Page<ProductPojo> selectByBrandCategory(Integer brandCategory, Integer page, Integer size) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_BY_BRAND_CATEGORY, ProductPojo.class);
		query.setParameter("brandCategory", brandCategory);

		int pageNumber = page;
		int pageSize = size;
		int firstResult = pageNumber * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);

		// execute the query
		List<ProductPojo> entities = query.getResultList();
		Long totalElements = em().createQuery(SELECT_BY_BRAND_CATEGORY_COUNT, Long.class).setParameter("brandCategory", brandCategory).getSingleResult();
		return new PageImpl<>(entities, PageRequest.of(page, size), totalElements);
	}
	
	public ProductPojo selectByName(String name) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_BY_NAME, ProductPojo.class);
		query.setParameter("name", name);
		return getSingle(query);
	}
	
	public List<ProductPojo> selectByMrp(Double mrp) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_BY_MRP, ProductPojo.class);
		query.setParameter("mrp", mrp);
		return query.getResultList();
	}
	
	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
		return query.getResultList();
	}
	public Page<ProductPojo> getAllByPage(Integer page, Integer size){
		TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
		// private static String select_all = "select p from ProductPojo p";apply pagination
		int pageNumber = page;
		int pageSize = size;
		int firstResult = pageNumber * pageSize;
		query.setFirstResult(firstResult);
		query.setMaxResults(pageSize);

		// execute the query
		List<ProductPojo> entities = query.getResultList();
		Long totalElements = em().createQuery(SELECT_ALL_COUNT, Long.class).getSingleResult();
		return new PageImpl<>(entities, PageRequest.of(page, size), totalElements);
	}
	public void update(ProductPojo productPojo) {
		// Implemented by Spring itself
	}

}
