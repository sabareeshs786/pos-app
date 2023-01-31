package com.increff.posapp.controller;

import java.util.List;

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

	@Autowired
	private BrandDto brandDto;

	@ApiOperation(value = "Adds a new item of a particular brand and category")
	@RequestMapping(path = "/api/brand", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		brandDto.add(form);
	}

	@ApiOperation(value = "Gets a brand by ID")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable int id) throws ApiException {
		return brandDto.get(id);
	}

	@ApiOperation(value = "Gets list of all brand and category")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public List<BrandData> getAll() throws ApiException {
		return brandDto.getAll();
	}

	@ApiOperation(value = "Gets list of all brand and category")
	@RequestMapping(path = "/api/brand/{page}/{size}", method = RequestMethod.GET)
	public Page<BrandData> getAll(@PathVariable Integer page, @PathVariable Integer size) throws ApiException {
		System.out.println("Page = "+page+"\nSize="+size);
		return brandDto.getAll(page, size);
	}

	@ApiOperation(value = "Updates a brand and category")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException {
		brandDto.updateById(id, form);
	}

}
