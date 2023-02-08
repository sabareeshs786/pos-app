package com.increff.posapp.controller;

import com.increff.posapp.dto.ProductDto;
import com.increff.posapp.model.ProductData;
import com.increff.posapp.model.ProductForm;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Api
@Validated
@RestController
public class ProductController {
	
	@Autowired
	private ProductDto productDto;
	private static final Logger logger = Logger.getLogger(ProductController.class);


	@ApiOperation(value = "Adds a product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public ProductData add(@Valid @RequestBody ProductForm form) throws ApiException {
		return productDto.add(form);
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

	@ApiOperation(value = "Gets a product by its barcode")
	@RequestMapping(path = "/api/product/{barcode}", method = RequestMethod.GET)
	public ProductData getByBarcode(@PathVariable String barcode) throws ApiException {
		return productDto.getByBarcode(barcode);
	}
	@ApiOperation(value = "Updates a product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public ProductData update(@PathVariable Integer id, @Valid @RequestBody ProductForm form) throws ApiException {
		return productDto.updateById(id, form);
	}

}
