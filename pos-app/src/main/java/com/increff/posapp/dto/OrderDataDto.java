package com.increff.posapp.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.posapp.model.OrderData;
import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.InventoryService;
import com.increff.posapp.service.OrderItemService;
import com.increff.posapp.service.OrderService;
import com.increff.posapp.service.ProductService;

@Component
public class OrderDataDto {

	@Autowired
	private ProductService productService;

	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderItemService orderItemService;
	
	public OrderData get(Integer orderId) throws ApiException {
		OrderPojo orderPojo = orderService.getById(orderId);
		return null;
	}
}
