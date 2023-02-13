package com.increff.posapp.service;

import com.increff.posapp.dao.PosDaySalesDao;
import com.increff.posapp.pojo.PosDaySalesPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional(rollbackOn = ApiException.class)
public class PosDaySalesService {

	@Autowired
	private PosDaySalesDao posDaySalesDao;

	public void add(PosDaySalesPojo p) throws ApiException {

		//Inserting
		posDaySalesDao.insert(p);
	}

	public ZonedDateTime getLastDateTime(){
		return posDaySalesDao.getLastDateTime();
	}

	public List<PosDaySalesPojo> getAll(){
		return posDaySalesDao.selectAll();
	}

	public Page<PosDaySalesPojo> getAllByPage(Integer page, Integer size){
		return posDaySalesDao.getAllByPage(page, size);
	}
}