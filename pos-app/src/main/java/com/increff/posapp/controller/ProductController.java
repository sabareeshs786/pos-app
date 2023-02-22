package com.increff.posapp.controller;

import com.increff.posapp.dto.ProductDto;
import com.increff.posapp.model.ProductData;
import com.increff.posapp.model.ProductForm;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Api
@RestController
public class ProductController {
	
	@Autowired
	private ProductDto productDto;
	private static final Logger logger = Logger.getLogger(ProductController.class);

	@ApiOperation(value = "Adds a product")
	@RequestMapping(path = "/api/products", method = RequestMethod.POST)
	public ProductData add(@RequestBody ProductForm form) throws ApiException, IllegalAccessException {
		return productDto.add(form);
	}

	@ApiOperation(value = "Updates a product")
	@RequestMapping(path = "/api/products/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable Integer id) throws ApiException, IllegalAccessException {
		return productDto.get(id);
	}

	@ApiOperation(value = "Gets the requested product data")
	@RequestMapping(path = "/api/products", method = RequestMethod.GET)
	public Object get(
			@RequestParam(required = false) String barcode,
			@RequestParam(name = "page-number", required = false) Integer page,
			@RequestParam(name = "page-size", required = false) Integer size
	) throws ApiException, UnsupportedEncodingException {
		return productDto.get(barcode, page, size);
	}

	@ApiOperation(value = "Updates a product")
	@RequestMapping(path = "/api/products/{id}", method = RequestMethod.PUT)
	public ProductData update(@PathVariable Integer id, @RequestBody ProductForm form) throws ApiException, IllegalAccessException {
		return productDto.update(id, form);
	}

}
