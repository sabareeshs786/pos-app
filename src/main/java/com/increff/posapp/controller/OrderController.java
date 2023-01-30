package com.increff.posapp.controller;

import java.util.List;

import com.increff.posapp.model.OrderItemEditForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.posapp.dto.OrderDto;
import com.increff.posapp.dto.OrderItemDto;
import com.increff.posapp.model.OrderData;
import com.increff.posapp.model.OrderForm;
import com.increff.posapp.model.OrderItemData;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderController {
	@Autowired
	private OrderItemDto orderItemDto;
	@Autowired
	private OrderDto orderDto;

	@ApiOperation(value = "Adds an order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public void add(@RequestBody OrderForm form) throws ApiException {
		System.out.println(form);
		orderDto.add(form);
	}

	@ApiOperation(value = "Gets list of all ordered items by order id")
	@RequestMapping(path = "/api/order/{orderId}", method = RequestMethod.GET)
	public List<OrderItemData> getByOrderId(@PathVariable Integer orderId) throws ApiException {
		return orderItemDto.getByOrderId(orderId);
	}

	@ApiOperation(value = "Gets list of all ordered items by order id")
	@RequestMapping(path = "/api/order/{orderId}/{page}/{size}", method = RequestMethod.GET)
	public Page<OrderItemData> getPageByOrderId(@PathVariable Integer orderId, @PathVariable Integer page, @PathVariable Integer size) throws ApiException {
		System.out.println("OrderId: "+orderId+"\nPage: "+page+"\nSize: "+size);
		return orderItemDto.getPageByOrderId(orderId, page, size);
	}

	@ApiOperation(value = "Gets list of all orders")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<OrderData> getAll() throws ApiException {
		return orderDto.getAll();
	}
	@ApiOperation(value = "Gets list of all orders")
	@RequestMapping(path = "/api/order/{pageNo}/{size}", method = RequestMethod.GET)
	public Page<OrderData> getAll(@PathVariable Integer pageNo, @PathVariable Integer size) throws ApiException {
		return orderDto.getAll(pageNo, size);
	}
	@ApiOperation(value = "Gets an order item by id")
	@RequestMapping(path = "/api/orderitems/{id}", method = RequestMethod.GET)
	public OrderItemData getOrderItem(@PathVariable Integer id) throws ApiException {
		return orderItemDto.getByOrderItemId(id);
	}
	@ApiOperation(value = "Edits an order item by id")
	@RequestMapping(path = "/api/orderitems/{id}", method = RequestMethod.PUT)
	public void updateOrderItem(@PathVariable Integer id, @RequestBody OrderItemEditForm orderItemEditForm) throws ApiException {
		orderItemDto.update(id, orderItemEditForm);
	}

}
