package com.increff.posapp.controller;

import com.increff.posapp.dto.PosDaySalesDto;
import com.increff.posapp.model.PosDaySalesData;
import com.increff.posapp.model.PosDaySalesForm;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class PosDaySalesController {

	@Autowired
	private PosDaySalesDto posDaySalesDto;
	private static final Logger logger = Logger.getLogger(PosDaySalesController.class);


	@ApiOperation(value = "Gets the daily sales report")
	@RequestMapping(path = "/api/reports/dailysalesreport", method = RequestMethod.GET)
	public List<PosDaySalesData> getData() throws ApiException{
		logger.info("Daily sales report controller!!");
		return posDaySalesDto.getAll();
	}

	@ApiOperation(value = "Gets the daily sales report between specified dates")
	@RequestMapping(path = "/api/reports/dailysalesreport/date", method = RequestMethod.POST)
	public List<PosDaySalesData> getDataByDate(@RequestBody PosDaySalesForm form) throws ApiException {
		logger.info("Daily sales report controller by date!!!");
		return posDaySalesDto.getData(form);
	}
}
