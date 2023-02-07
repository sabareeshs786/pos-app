package com.increff.posapp.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.increff.posapp.dao.OrderDao;
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

	public List<OrderPojo> getByStartTime(String startTime) throws ApiException {
		return getCheckByStartTime(startTime);
	}

	public List<OrderPojo> getByEndTime(String endTime) throws ApiException {
		return getCheckByEndTime(endTime);
	}

	public List<OrderPojo> getByInterval(ZonedDateTime startTime, ZonedDateTime endTime) throws ApiException {
		return getCheckByInterval(startTime, endTime);
	}
	public List<OrderPojo> getByTime(ZonedDateTime time) throws ApiException {
		return getCheckByTime(time);
	}

	public List<OrderPojo> getAll() {
		return orderDao.selectAll();
	}

	public Page<OrderPojo> getAllByPage(Integer page, Integer size){
		return orderDao.getAllByPage(page, size);
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

	public List<OrderPojo> getCheckByStartTime(String startDate) throws ApiException {
		List<OrderPojo> list = orderDao.selectByStartTime(startDate);
		if(list.isEmpty()) {
			throw new ApiException("Order with the given start time doesn't exist");
		}
		return list;
	}

	public List<OrderPojo> getCheckByEndTime(String endDate) throws ApiException {
		List<OrderPojo> list = orderDao.selectByEndTime(endDate);
		if(list.isEmpty()) {
			throw new ApiException("Order before the given end time doesn't exist");
		}
		return list;
	}

	public List<OrderPojo> getCheckByInterval(ZonedDateTime startDate, ZonedDateTime endDate) throws ApiException {
		return orderDao.selectByInterval(startDate, endDate);
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
//		if(p.getTime().getZone().equals(ZoneId.systemDefault())){
//			throw new ApiException("Both the zones are different");
//		}
	}
}
