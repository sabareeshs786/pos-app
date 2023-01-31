package com.increff.posapp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import com.increff.posapp.pojo.ProductPojo;

@Repository
public class ProductInMemDao{
	
	private HashMap<Integer, ProductPojo> rows;
	private Integer lastId;

	@PostConstruct
	public void init() {
		rows = new HashMap<Integer, ProductPojo>();
	}
	
	public void insert(ProductPojo p) {
		lastId++;
		p.setId(lastId);
		rows.put(lastId, p);
	}

	public void delete(Integer id) {
		rows.remove(id);
	}

	public ProductPojo select(Integer id) {
		return rows.get(id);
	}
	
	public List<ProductPojo> selectAll() {
		ArrayList<ProductPojo> list = new ArrayList<ProductPojo>();
		list.addAll(rows.values());
		return list;
	}

	public void update(Integer id, ProductPojo p) {
		rows.put(id, p);
	}


}
