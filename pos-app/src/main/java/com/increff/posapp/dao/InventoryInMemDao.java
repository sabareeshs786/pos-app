package com.increff.posapp.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import com.increff.posapp.pojo.InventoryPojo;

@Repository
public class InventoryInMemDao{
	
	private HashMap<Integer, InventoryPojo> rows;

	@PostConstruct
	public void init() {
		rows = new HashMap<Integer, InventoryPojo>();
	}
	
	public void insert(InventoryPojo p) {
	}

	public void delete(Integer id) {
		rows.remove(id);
	}

	public InventoryPojo select(Integer id) {
		return rows.get(id);
	}
	
	public List<InventoryPojo> selectAll() {
		ArrayList<InventoryPojo> list = new ArrayList<InventoryPojo>();
		list.addAll(rows.values());
		return list;
	}

	public void update(Integer id, InventoryPojo p) {
		rows.put(id, p);
	}


}
