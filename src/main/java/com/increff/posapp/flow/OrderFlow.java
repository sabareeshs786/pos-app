package com.increff.posapp.flow;

import com.increff.posapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.posapp.dto.OrderDataDto;
import com.increff.posapp.dto.OrderDto;
import com.increff.posapp.dto.OrderItemDto;
import com.increff.posapp.model.OrderForm;
import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.util.FormValidator;

import java.util.List;

@Component
public class OrderFlow {
	
	@Autowired
	private OrderDto orderDto;
	
	@Autowired
	private OrderDataDto orderDataDto;
	
	@Autowired
	private OrderItemDto orderItemDto;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private OrderItemService orderItemService;
	
	public void addOrderDataFlow(OrderForm[] forms) throws ApiException {
		
		OrderPojo orderPojo = orderDto.add();
		for(OrderForm form: forms) {
			FormValidator.orderFormValidator(form);
			ProductPojo productPojo = productService.getByBarcode(form.getBarcode());
			InventoryPojo inventoryPojo = inventoryService.getByPid(productPojo.getId());
			Integer productId = inventoryPojo.getProductId();
			Double mrp = productPojo.getMrp();
			if (form.getSellingPrice() > mrp) {
				throw new ApiException("Selling price must be less than MRP");
			}
			Integer initialQuantity = inventoryPojo.getQuantity();
			if (initialQuantity < form.getQuantity()) {
				throw new ApiException("Entered quantity is greater than quantity present in inventory");
			}
			Integer finalQuantity = initialQuantity - form.getQuantity();
			inventoryPojo.setQuantity(finalQuantity);
			inventoryService.updateByPid(productId, inventoryPojo);
			orderItemDto.add(form, orderPojo, productId);
		}
	}

	public void deleteOrderDataFlow(Integer id) throws ApiException {
		List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(id);
		for(OrderItemPojo orderItemPojo: orderItemPojoList){
			InventoryPojo inventoryPojo = inventoryService.getByPid(orderItemPojo.getProductId());
			inventoryPojo.setQuantity(inventoryPojo.getQuantity() + orderItemPojo.getQuantity());
			inventoryService.updateByPid(inventoryPojo.getProductId(), inventoryPojo);
			orderItemService.deleteById(orderItemPojo.getId());
		}
		orderService.deleteById(id);
	}
}
