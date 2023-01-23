package com.increff.posapp.controller;

import java.util.List;

import com.increff.posapp.flow.OrderFlow;
import com.increff.posapp.model.OrderItemsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.posapp.dto.OrderDataDto;
import com.increff.posapp.dto.OrderDto;
import com.increff.posapp.dto.OrderItemDto;
import com.increff.posapp.model.OrderData;
import com.increff.posapp.model.OrderForm;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.InventoryService;
import com.increff.posapp.service.OrderItemService;
import com.increff.posapp.service.OrderService;
import com.increff.posapp.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderController {

	@Autowired
	private ProductService productService;

	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;


	@Autowired
	private OrderItemDto orderItemDto;
	
	@Autowired
	private OrderDataDto orderDataDto;
	
	@Autowired
	private OrderFlow orderFlow;
	
	@Autowired
	private OrderDto orderDto;

	@ApiOperation(value = "Adds an order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public void add(@RequestBody OrderForm[] forms) throws ApiException {
		orderFlow.addOrderDataFlow(forms);
	}

//	@ApiOperation(value = "Deletes an order with the order id")
//	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
//	public void delete(@PathVariable int id) throws ApiException {
//		orderFlow.deleteOrderDataFlow(id);
//	}

	@ApiOperation(value = "Gets an order by ID")
	@RequestMapping(path = "/api/order/{orderId}", method = RequestMethod.GET)
	public List<OrderItemsData> getByOrderId(@PathVariable int orderId) throws ApiException {
		return orderItemDto.getByOrderId(orderId);
	}

	@ApiOperation(value = "Gets list of all orders")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<OrderData> getAll() throws ApiException {
		return orderDto.getAll();
	}

	@ApiOperation(value = "Updates a order")
	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderForm f) {

	}
}
