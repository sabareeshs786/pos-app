package com.increff.posapp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import com.increff.posapp.pojo.OrderItemPojo;

@Repository
public class OrderItemInMemDao{
	
	private HashMap<Integer, OrderItemPojo> rows;
	private Integer lastId;

	@PostConstruct
	public void init() {
		rows = new HashMap<Integer, OrderItemPojo>();
	}
	
	public void insert(OrderItemPojo p) {
		lastId++;
		p.setId(lastId);
		rows.put(lastId, p);
	}

	public void delete(Integer id) {
		rows.remove(id);
	}

	public OrderItemPojo select(Integer id) {
		return rows.get(id);
	}
	
	public List<OrderItemPojo> selectAll() {
		ArrayList<OrderItemPojo> list = new ArrayList<OrderItemPojo>();
		list.addAll(rows.values());
		return list;
	}

	public void update(Integer id, OrderItemPojo p) {
		rows.put(id, p);
	}


}
