package com.increff.posapp.dto;

import com.increff.posapp.model.OrderData;
import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.OrderItemService;
import com.increff.posapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.OrderService;
import com.increff.posapp.util.ConverterDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDto {

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;
	
	public OrderPojo add() throws ApiException {
		OrderPojo orderPojo = ConverterDto.convertToOrderPojo();
		orderService.add(orderPojo);
		return orderPojo;
	}

	public List<OrderData> getAll() throws ApiException {
		List<OrderPojo> list = orderService.getAll();
		List<OrderData> list2 = new ArrayList<OrderData>();
		Double totalAmount;
		for (OrderPojo orderPojo : list) {
			totalAmount = 0.00;
			for(OrderItemPojo orderItemPojo: orderItemService.getByOrderId(orderPojo.getId())){
				totalAmount += orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity();
			}
			list2.add(ConverterDto.convertToOrderData(orderPojo, totalAmount));
		}
		return list2;
	}
	public void update(Integer id){
		OrderPojo p = new OrderPojo();
	}
}
