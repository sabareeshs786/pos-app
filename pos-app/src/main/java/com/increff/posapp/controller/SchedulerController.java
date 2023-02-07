package com.increff.posapp.controller;

import com.increff.posapp.dto.ProductDto;
import com.increff.posapp.dto.SchedulerDto;
import com.increff.posapp.model.ProductData;
import com.increff.posapp.model.ProductForm;
import com.increff.posapp.model.SchedulerData;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

@Api
@RestController
public class SchedulerController {

	private SchedulerDto schedulerDto;
	private static final Logger logger = Logger.getLogger(SchedulerController.class);


	@ApiOperation(value = "Gets the requested product data")
	@RequestMapping(path = "/api/scheduler", method = RequestMethod.GET)
	public Page<SchedulerData> getData(
			@RequestParam(required = false) LocalDateTime startDate,
			@RequestParam(required = false) LocalDateTime endDate,
			@RequestParam(name = "pagenumber", required = false) Integer page,
			@RequestParam(required = false) Integer size) throws ApiException{
//		return schedulerDto.getData();
		return null;
	}
}
