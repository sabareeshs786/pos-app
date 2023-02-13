package com.increff.posapp.controller;

import com.increff.posapp.dto.PosDaySalesDto;
import com.increff.posapp.model.PosDaySalesData;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class PosDaySalesController {

	@Autowired
	private PosDaySalesDto posDaySalesDto;
	private static final Logger logger = Logger.getLogger(PosDaySalesController.class);


	@ApiOperation(value = "Gets the requested product data")
	@RequestMapping(path = "/api/reports/scheduler", method = RequestMethod.GET)
	public List<PosDaySalesData> getData() throws ApiException{
		logger.info("Scheduler!!!");
		return posDaySalesDto.getAll();
	}
}
