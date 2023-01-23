package com.increff.posapp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.increff.posapp.service.ApiException;
import org.springframework.stereotype.Repository;

import com.increff.posapp.pojo.ProductPojo;

@Repository
@Transactional(rollbackOn = ApiException.class)
public class ProductDao extends AbstractDao{
	
	private static String delete_id = "delete from ProductPojo p where id=:id";
	private static String delete_barcode = "delete from ProductPojo p where barcode=:barcode";
	private static String delete_brand_catecory = "delete from ProductPojo p where brand_category=:brand_category";
	private static String delete_name = "delete from ProductPojo p where name=:name";
	private static String delete_mrp = "delete from ProductPojo p where mrp=:mrp";
	
	private static String select_id = "select p from ProductPojo p where id=:id";
	private static String select_barcode = "select p from ProductPojo p where barcode=:barcode";
	private static String select_brand_catecory = "select p from ProductPojo p where brand_category=:brand_category";
	private static String select_name = "select p from ProductPojo p where name=:name";
	private static String select_mrp = "select p from ProductPojo p where mrp=:mrp";
	private static String select_all = "select p from ProductPojo p";


	@Transactional
	public void insert(ProductPojo productPojo) {
		em().persist(productPojo);
	}

	public Integer deleteById(Integer id) {
		Query query = em().createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}
	public Integer deleteByBarcode(String barcode) {
		Query query = em().createQuery(delete_barcode);
		query.setParameter("barcode", barcode);
		return query.executeUpdate();
	}
	
	public Integer deleteByBrandCategory(Integer brand_category) {
		Query query = em().createQuery(delete_brand_catecory);
		query.setParameter("brand_category", brand_category);
		return query.executeUpdate();
	}
	
	public Integer deleteByName(String name) {
		Query query = em().createQuery(delete_name);
		query.setParameter("name", name);
		return query.executeUpdate();
	}
	
	public Integer deleteByMrp(Double mrp) {
		Query query = em().createQuery(delete_mrp);
		query.setParameter("mrp", mrp);
		return query.executeUpdate();
	}
	
	public ProductPojo selectById(Integer id) {
		TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}
	
	public ProductPojo selectByBarcode(String barcode) {
		TypedQuery<ProductPojo> query = getQuery(select_barcode, ProductPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}
	
	public List<ProductPojo> selectByBrandCategory(Integer brand_category) {
		TypedQuery<ProductPojo> query = getQuery(select_brand_catecory, ProductPojo.class);
		query.setParameter("brand_category", brand_category);
		return query.getResultList();
	}
	
	public ProductPojo selectByName(String name) {
		TypedQuery<ProductPojo> query = getQuery(select_name, ProductPojo.class);
		query.setParameter("name", name);
		return getSingle(query);
	}
	
	public List<ProductPojo> selectByMrp(Double mrp) {
		TypedQuery<ProductPojo> query = getQuery(select_mrp, ProductPojo.class);
		query.setParameter("mrp", mrp);
		return query.getResultList();
	}
	
	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
		return query.getResultList();
	}

	public void update(ProductPojo productPojo) {
		// Implemented by Spring itself
	}

}
