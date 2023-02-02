package com.increff.posapp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

	@ApiOperation(value = "Adds a product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		productDto.add(form);
	}

	@ApiOperation(value = "Gets a product by product id")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
	public ProductData getById(@PathVariable Integer id) throws ApiException, UnsupportedEncodingException {
//		Integer id = null;
//		String decodedPath = URLDecoder.decode(query, StandardCharsets.UTF_8.toString());
//		String[] querySplit = decodedPath.split("\\?(?!\\?)");
//		String[] queries = querySplit[1].split("=");
//		if(queries[0].equals("id")){
//			id = Integer.parseInt(queries[1]);
//		}
		return productDto.getById(id);
	}
	
//	@ApiOperation(value = "Gets a product by barcode")
//	@RequestMapping(path = "/api/product?barcode={barcode}", method = RequestMethod.GET)
//	public ProductData getByBarcode(@PathVariable String barcode) throws ApiException {
//		return productDto.getByBarcode(barcode);
//	}
	
	@ApiOperation(value = "Gets list of all products")
	@RequestMapping(path = "/api/product/{pageNo}/{size}", method = RequestMethod.GET)
	public Page<ProductData> getAll(@PathVariable Integer pageNo, @PathVariable Integer size) throws ApiException {
		return productDto.getAll(pageNo, size);
	}

	@ApiOperation(value = "Updates a product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer id, @RequestBody ProductForm form) throws ApiException {
		productDto.updateById(id, form);
	}

}
