package com.increff.posapp.service;

import com.increff.posapp.dao.SchedulerDao;
import com.increff.posapp.pojo.PosDaySalesPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class SchedulerService {

	@Autowired
	private SchedulerDao schedulerDao;

	public void add(PosDaySalesPojo p) throws ApiException {
		//Inserting
		schedulerDao.insert(p);
	}

	public ZonedDateTime getLastDateTime(){
		return schedulerDao.getLastDateTime();
	}

	public List<PosDaySalesPojo> getAll(){
		return schedulerDao.selectAll();
	}

	public Page<PosDaySalesPojo> getAllByPage(Integer page, Integer size){
		return schedulerDao.getAllByPage(page, size);
	}
}