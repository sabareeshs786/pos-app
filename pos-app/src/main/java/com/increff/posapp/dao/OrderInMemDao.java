package com.increff.posapp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import com.increff.posapp.pojo.OrderPojo;

@Repository
public class OrderInMemDao{
	
	private HashMap<Integer, OrderPojo> rows;
	private Integer lastId;

	@PostConstruct
	public void init() {
		rows = new HashMap<Integer, OrderPojo>();
	}
	
	public void insert(OrderPojo p) {
		lastId++;
		p.setId(lastId);
		rows.put(lastId, p);
	}

	public void delete(Integer id) {
		rows.remove(id);
	}

	public OrderPojo select(Integer id) {
		return rows.get(id);
	}
	
	public List<OrderPojo> selectAll() {
		ArrayList<OrderPojo> list = new ArrayList<OrderPojo>();
		list.addAll(rows.values());
		return list;
	}

	public void update(Integer id, OrderPojo p) {
		rows.put(id, p);
	}


}
