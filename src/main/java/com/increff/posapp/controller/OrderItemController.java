package com.increff.posapp.controller;

import com.increff.posapp.dto.OrderDataDto;
import com.increff.posapp.dto.OrderDto;
import com.increff.posapp.dto.OrderItemDto;
import com.increff.posapp.flow.OrderFlow;
import com.increff.posapp.model.*;
import com.increff.posapp.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class OrderItemController {

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

//	@ApiOperation(value = "Adds an order")
//	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
//	public void add(@RequestBody OrderForm[] forms) throws ApiException {
//			orderFlow.addOrderDataFlow(forms);
//	}

//	@ApiOperation(value = "Deletes an order with the order id")
//	@RequestMapping(path = "/api/order/{id}", method = RequestMethod.DELETE)
//	public void delete(@PathVariable int id) throws ApiException {
//		orderFlow.deleteOrderDataFlow(id);
//	}

	@ApiOperation(value = "Gets an order by ID")
	@RequestMapping(path = "/api/orderitems/{id}", method = RequestMethod.GET)
	public OrderItemData getById(@PathVariable int id) throws ApiException {
		return orderItemDto.getByOrderItemId(id);
	}

	@ApiOperation(value = "Gets list of all orders")
	@RequestMapping(path = "/api/orderitems", method = RequestMethod.GET)
	public List<OrderItemData> getAll() throws ApiException {
		return orderItemDto.getAll();
	}

	@ApiOperation(value = "Updates a order")
	@RequestMapping(path = "/api/orderitems/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody OrderItemEditForm f) throws ApiException {
		orderItemDto.update(id, f);
	}
}
