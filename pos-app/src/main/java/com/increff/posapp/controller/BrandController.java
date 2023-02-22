package com.increff.posapp.controller;

import com.increff.posapp.dto.BrandDto;
import com.increff.posapp.model.BrandData;
import com.increff.posapp.model.BrandForm;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api
@RestController
public class BrandController {
	private static final Logger logger = Logger.getLogger(BrandController.class);
	@Autowired
	private BrandDto brandDto;

	@ApiOperation(value = "Adds a new brand and category")
	@RequestMapping(path = "/api/brands", method = RequestMethod.POST)
	public BrandData add(@RequestBody BrandForm form) throws ApiException, IllegalAccessException {
		return brandDto.add(form);
	}

	@ApiOperation(value = "Gets brand and category by id")
	@RequestMapping(path = "/api/brands/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable Integer id) throws ApiException, IllegalAccessException {
		return brandDto.get(id);
	}

	@ApiOperation(value = "Gets brand and category in a particular page")
	@RequestMapping(path = "/api/brands", method = RequestMethod.GET)
	public Page<BrandData> get(
			@RequestParam(name = "page-number", required = false) Integer page,
			@RequestParam(name = "page-size", required = false) Integer size
	) throws ApiException {
		return brandDto.get(page, size);
	}

	@ApiOperation(value = "Updates a brand and category")
	@RequestMapping(path = "/api/brands/{id}", method = RequestMethod.PUT)
	public BrandData update(@PathVariable int id, @Valid @RequestBody BrandForm form) throws ApiException, IllegalAccessException {
		return brandDto.update(id, form);
	}

}