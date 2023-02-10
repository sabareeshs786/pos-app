package com.increff.posapp.service;

import com.increff.posapp.dao.OrderDao;
import com.increff.posapp.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

	public List<OrderPojo> getByInterval(ZonedDateTime startTime, ZonedDateTime endTime) throws ApiException {
		validateTimeInterval(startTime, endTime);
		return getCheckByInterval(startTime, endTime);
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

	private OrderPojo getCheckById(Integer id) throws ApiException {
		OrderPojo p = orderDao.selectById(id);
		if (p == null) {
			throw new ApiException("Brand Category combination with given ID does not exit, id: " + id);
		}
		return p;
	}

	private List<OrderPojo> getCheckByInterval(ZonedDateTime startDate, ZonedDateTime endDate) throws ApiException {
		return orderDao.selectByInterval(startDate, endDate);
	}
	private void validate(OrderPojo p) throws ApiException {
		if(p.getTime() == null) {
			throw new ApiException("Date and Time cannot be null");
		}
	}

	private void validateTimeInterval(ZonedDateTime startTime, ZonedDateTime endTime) throws ApiException {
		if(endTime.toLocalDate().isAfter(LocalDate.now())){
			throw new ApiException("End date should be today's date or a date before today");
		}
		if(ChronoUnit.DAYS.between(startTime.toLocalDate(), endTime.toLocalDate()) > 366){
			throw new ApiException("Difference between the two entered dates must be within one year");
		}
	}
}
