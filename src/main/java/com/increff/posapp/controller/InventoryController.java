package com.increff.posapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

	@ApiOperation(value = "Adds a new product and it's quantity")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws ApiException {
		inventoryDto.add(form);
	}

	
	@ApiOperation(value = "Deletes an inventory by id")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable Integer id) throws ApiException {
		inventoryDto.deleteById(id);
	}

	@ApiOperation(value = "Gets a inventory by id")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable Integer id) throws ApiException {
		return inventoryDto.getById(id);
	}

	@ApiOperation(value = "Gets the whole inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException {
		return inventoryDto.getAll();
	}

	@ApiOperation(value = "Updates the inventory by id")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer id, @RequestBody InventoryForm f) throws ApiException {
		inventoryDto.updateById(id, f);
	}

}
