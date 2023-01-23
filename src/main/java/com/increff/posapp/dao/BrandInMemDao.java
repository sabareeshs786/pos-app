package com.increff.posapp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import org.hibernate.exception.ConstraintViolationException;

import com.increff.posapp.pojo.BrandPojo;

@Repository
public class BrandInMemDao{
	
	private HashMap<Integer, BrandPojo> rows;
	private Integer lastId;

	@PostConstruct
	public void init() {
		rows = new HashMap<Integer, BrandPojo>();
	}
	
	public void insert(BrandPojo p) {
		lastId++;
		p.setId(lastId);
		try {
		rows.put(lastId, p);
		} catch(ConstraintViolationException e) {
			System.out.println(e);
		}
	}

	public void delete(Integer id) {
		rows.remove(id);
	}

	public BrandPojo select(Integer id) {
		return rows.get(id);
	}
	
	public List<BrandPojo> selectAll() {
		ArrayList<BrandPojo> list = new ArrayList<BrandPojo>();
		list.addAll(rows.values());
		return list;
	}

	public void update(Integer id, BrandPojo p) {
		rows.put(id, p);
	}


}
