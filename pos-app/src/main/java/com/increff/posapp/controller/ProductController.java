package com.increff.posapp.controller;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.increff.posapp.dto.ProductDto;
import com.increff.posapp.model.ProductData;
import com.increff.posapp.model.ProductForm;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductController {
	
	@Autowired
	private ProductDto productDto;
	private static Logger logger = Logger.getLogger(ProductController.class);


	@ApiOperation(value = "Adds a product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		productDto.add(form);
	}

	@ApiOperation(value = "Gets the requested product data")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public Page<ProductData> getData(
			@RequestParam(required = false) Integer id,
			@RequestParam(name = "pagenumber", required = false) Integer page,
			@RequestParam(required = false) Integer size
	) throws ApiException, UnsupportedEncodingException {
		logger.info("Id="+id+"Page number="+page+"Size="+size);
		return productDto.getData(id, page, size);
	}

	@ApiOperation(value = "Updates a product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer id, @RequestBody ProductForm form) throws ApiException {
		productDto.updateById(id, form);
	}

}
