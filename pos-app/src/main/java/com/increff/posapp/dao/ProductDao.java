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
	private static final String SELECT_BY_ID = "select p from ProductPojo p where id=:id";
	private static final String SELECT_BY_BARCODE = "select p from ProductPojo p where barcode=:barcode";
	private static final String SELECT_ALL = "select p from ProductPojo p";
	private static final String SELECT_ALL_COUNT = "select count(p) from ProductPojo p";


	@Transactional
	public ProductPojo insert(ProductPojo productPojo) {
		em().persist(productPojo);
		return productPojo;
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
