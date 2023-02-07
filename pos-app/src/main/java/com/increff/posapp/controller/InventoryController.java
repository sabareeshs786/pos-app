package com.increff.posapp.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.increff.posapp.dto.InventoryDto;
import com.increff.posapp.model.InventoryData;
import com.increff.posapp.model.InventoryForm;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryController {
	
	@Autowired
	private InventoryDto inventoryDto;
	private static final Logger logger = Logger.getLogger(InventoryController.class);

	@ApiOperation(value = "Adds a new product and it's quantity")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws ApiException {
		inventoryDto.add(form);
	}

	@ApiOperation(value = "Gets the requested inventory data")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public Page<InventoryData> get(
			@RequestParam(required = false) Integer productId,
			@RequestParam(name = "pagenumber", required = false) Integer page,
			@RequestParam(required = false) Integer size
	) throws ApiException {
		logger.info("ProductId="+productId+"Page number="+page+"Size="+size);
		return inventoryDto.getData(productId, page, size);
	}

	@ApiOperation(value = "Updates the inventory by id")
	@RequestMapping(path = "/api/inventory/{productId}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer productId, @RequestBody InventoryForm f) throws ApiException {
		inventoryDto.updateByProductId(productId, f);
	}

}
