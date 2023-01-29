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
	public void add(OrderForm form) throws ApiException {

		OrderPojo orderPojo = new OrderPojo("Asia/Kolkata");
		orderService.add(orderPojo);
		Integer len = form.getBarcodes().size();
		for(int i=0; i < len; i++) {
			FormValidator.orderFormValidator(form);

			ProductPojo productPojo = productService.getByBarcode(form.getBarcodes().get(i));
			InventoryPojo inventoryPojo = inventoryService.getByProductId(productPojo.getId());
			Integer productId = inventoryPojo.getProductId();
			Double mrp = productPojo.getMrp();
			Integer initialQuantity = inventoryPojo.getQuantity();

			validateMrp(form.getSellingPrices().get(i), mrp);
			validateQuantityRequest(form.getQuantities().get(i), initialQuantity);

			Integer finalQuantity = initialQuantity - form.getQuantities().get(i);
			inventoryPojo.setQuantity(finalQuantity);
			inventoryService.updateByProductId(productId, inventoryPojo);

			OrderItemPojo orderItemPojo = ConverterDto.convertToOrderItemPojo(form, i, orderPojo, productId);
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

	private void validateMrp(Double sellingPrice, Double mrp) throws ApiException {
		if (sellingPrice > mrp) {
			throw new ApiException("Selling price must be less than MRP");
		}
	}

	private void validateQuantityRequest(Integer requestedQuantity, Integer initialQuantity) throws ApiException {
		if (initialQuantity < requestedQuantity) {
			throw new ApiException("Entered quantity is greater than quantity present in inventory");
		}
	}
}
