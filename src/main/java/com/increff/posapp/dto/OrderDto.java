package com.increff.posapp.dto;

import com.increff.posapp.model.OrderData;
import com.increff.posapp.model.OrderForm;
import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.*;
import com.increff.posapp.util.FormNormalizer;
import com.increff.posapp.util.FormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.util.ConverterDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderDto {

	@Autowired
	private ProductService productService;

	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Transactional(rollbackOn = ApiException.class)
	public void add(OrderForm[] forms) throws ApiException {

		OrderPojo orderPojo = new OrderPojo("Asia/Kolkata");
		orderService.add(orderPojo);

		for(OrderForm form: forms) {
			FormValidator.orderFormValidator(form);
			FormNormalizer.orderFormNormalizer(form);

			ProductPojo productPojo = productService.getByBarcode(form.getBarcode());
			InventoryPojo inventoryPojo = inventoryService.getByProductId(productPojo.getId());
			Integer productId = inventoryPojo.getProductId();
			Double mrp = productPojo.getMrp();
			Integer initialQuantity = inventoryPojo.getQuantity();

			validateMrp(form, mrp);
			validateQuantityRequest(form, initialQuantity);

			Integer finalQuantity = initialQuantity - form.getQuantity();
			inventoryPojo.setQuantity(finalQuantity);
			inventoryService.updateByProductId(productId, inventoryPojo);

			OrderItemPojo orderItemPojo = ConverterDto.convertToOrderItemPojo(form, orderPojo, productId);
			orderItemService.add(orderItemPojo);
		}

	}

	public List<OrderData> getAll() throws ApiException {
		List<OrderPojo> list = orderService.getAll();
		List<OrderData> list2 = new ArrayList<OrderData>();
		Double totalAmount = 0.0;
		for (OrderPojo orderPojo : list) {
			totalAmount = 0.00;
			for(OrderItemPojo orderItemPojo: orderItemService.getByOrderId(orderPojo.getId())){
				totalAmount += orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity();
			}
			list2.add(ConverterDto.convertToOrderData(orderPojo, totalAmount));
		}
		return list2;
	}

	private void validateMrp(OrderForm form, Double mrp) throws ApiException {
		if (form.getSellingPrice() > mrp) {
			throw new ApiException("Selling price must be less than MRP");
		}
	}

	private void validateQuantityRequest(OrderForm form, Integer initialQuantity) throws ApiException {
		if (initialQuantity < form.getQuantity()) {
			throw new ApiException("Entered quantity is greater than quantity present in inventory");
		}
	}
}
