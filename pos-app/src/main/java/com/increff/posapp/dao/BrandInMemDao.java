package com.increff.posapp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.hibernate.exception.ConstraintViolationException;

import com.increff.posapp.pojo.BrandPojo;

@Repository
public class BrandInMemDao{

	private static Logger logger = Logger.getLogger(BrandInMemDao.class);
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
			logger.info(e);
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
