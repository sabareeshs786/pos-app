package com.increff.posapp.controller;

import com.increff.posapp.dto.ProductDto;
import com.increff.posapp.dto.SchedulerDto;
import com.increff.posapp.model.ProductData;
import com.increff.posapp.model.ProductForm;
import com.increff.posapp.model.SchedulerData;
import com.increff.posapp.model.SchedulerForm;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

@Api
@RestController
public class SchedulerController {

	@Autowired
	private SchedulerDto schedulerDto;
	private static final Logger logger = Logger.getLogger(SchedulerController.class);


	@ApiOperation(value = "Gets the requested product data")
	@RequestMapping(path = "/api/reports/scheduler", method = RequestMethod.GET)
	public List<SchedulerData> getData() throws ApiException{
		logger.info("Scheduler!!!");
		return schedulerDto.getAll();
	}
}
