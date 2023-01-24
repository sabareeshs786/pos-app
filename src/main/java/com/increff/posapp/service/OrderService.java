package com.increff.posapp.service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import com.increff.posapp.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.posapp.dao.OrderDao;
import com.increff.posapp.dao.OrderItemDao;
import com.increff.posapp.pojo.OrderPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderService {

	@Autowired
	private OrderDao orderDao;

	public void add(OrderPojo p) throws ApiException {
		validate(p);

		// Inserting the order
		orderDao.insert(p);
	}

	public OrderPojo getById(Integer id) throws ApiException {
		return getCheckById(id);
	}

	public List<OrderPojo> getByTime(ZonedDateTime time) throws ApiException {
		return getCheckByTime(time);
	}

	public List<OrderPojo> getAll() {
		return orderDao.selectAll();
	}

	public void updateById(Integer id, OrderPojo p) throws ApiException {
		OrderPojo ex = getCheckById(id);
		ex.setTime(p.getTime());
		orderDao.update(ex);
	}

	public void updateByTime(ZonedDateTime time, OrderPojo p) throws ApiException {
		List<OrderPojo> list = getCheckByTime(time);
		for(OrderPojo ex: list) {
		ex.setTime(p.getTime());
		orderDao.update(ex);
		}
	}

	public OrderPojo getCheckById(Integer id) throws ApiException {
		OrderPojo p = orderDao.selectById(id);
		if (p == null) {
			throw new ApiException("Brand Category combination with given ID does not exit, id: " + id);
		}
		return p;
	}

	public List<OrderPojo> getCheckByTime(ZonedDateTime time) throws ApiException {
		List<OrderPojo> list = orderDao.selectByTime(time);
		if(list.isEmpty()) {
			throw new ApiException("Order with the given time doesn't exist");
		}
		return list;
	}

	private void validate(OrderPojo p) throws ApiException {
		if(p.getTime() == null) {
			throw new ApiException("Date and Time cannot be null");
		}
	}
}
