package com.increff.posapp.controller;

import com.increff.posapp.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.increff.posapp.dto.BrandDto;
import com.increff.posapp.model.BrandData;
import com.increff.posapp.model.BrandForm;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandController {
	private static final Logger logger = Logger.getLogger(BrandController.class);
	@Autowired
	private BrandDto brandDto;

	@ApiOperation(value = "Adds a new item of a particular brand and category")
	@RequestMapping(path = "/api/brand", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		StringUtil.checkValid(form.getBrand());
		StringUtil.checkValid(form.getCategory());
		brandDto.add(form);
	}

	@ApiOperation(value = "Gets the requested brand data")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public Page<BrandData> getData(
			@RequestParam(required = false) Integer id,
			@RequestParam(name = "pagenumber", required = false) Integer page,
			@RequestParam(required = false) Integer size
	) throws ApiException {
		logger.info("Id="+id+"Page number="+page+"Size="+size);
		return brandDto.getData(id, page, size);
	}

	@ApiOperation(value = "Updates a brand and category")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException {
		brandDto.updateById(id, form);
	}

}
