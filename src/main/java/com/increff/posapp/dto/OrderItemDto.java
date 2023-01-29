package com.increff.posapp.dto;

import com.increff.posapp.model.OrderItemData;
import com.increff.posapp.model.OrderItemEditForm;
import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.*;
import com.increff.posapp.util.DateTimeUtil;
import com.increff.posapp.util.FormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.util.ConverterDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class OrderItemDto {
	
	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private InventoryService inventoryService;

	public List<OrderItemData> getByOrderId(Integer orderId) throws ApiException {
		List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderId);
		List<OrderItemData> list = new ArrayList<>();
		for(OrderItemPojo orderItemPojo:orderItemPojoList){
			ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
			list.add(ConverterDto.convertToOrderItemData(orderItemPojo, productPojo));
		}
		return list;
	}

	public OrderItemData getByOrderItemId(Integer id) throws ApiException {
		OrderItemPojo orderItemPojo = orderItemService.getById(id);
		ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
		return ConverterDto.convertToOrderItemData(orderItemPojo, productPojo);
	}

	public List<OrderItemData> getAll() throws ApiException {
		List<OrderItemPojo> orderItemPojoList = orderItemService.getAll();
		List<OrderItemData> list = new ArrayList<>();
		for(OrderItemPojo orderItemPojo : orderItemPojoList){
			ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
			list.add(ConverterDto.convertToOrderItemData(orderItemPojo, productPojo));
		}
		return list;
	}
	public void update(Integer id, OrderItemEditForm orderItemEditForm) throws ApiException {
		FormValidator.orderItemEditFormValidator(orderItemEditForm);
		isBarcodeValid(orderItemEditForm);
		OrderItemPojo orderItemPojo = orderItemService.getById(id);
		if(isProductChange(orderItemPojo, orderItemEditForm)){
			checkInventory(orderItemEditForm);
			isSellingPriceValid(orderItemEditForm);
			updateProduct(orderItemPojo, orderItemEditForm);
		}
		else if(isQuantityChange(orderItemPojo, orderItemEditForm)){
			isSellingPriceValid(orderItemEditForm);
			updateInventory(orderItemPojo, orderItemEditForm);
		}
		else if(isSellingPriceChange(orderItemPojo, orderItemEditForm)){
			isSellingPriceValid(orderItemEditForm);
			updateSellingPrice(orderItemPojo, orderItemEditForm);
		}
	}

	// Change detection
	private boolean isProductChange(OrderItemPojo orderItemPojo, OrderItemEditForm orderItemEditForm) throws ApiException {
		ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
		if(orderItemEditForm.getBarcode().equals(productPojo.getBarcode())){
			return false;
		}
		return true;
	}

	private boolean isQuantityChange(OrderItemPojo orderItemPojo, OrderItemEditForm orderItemEditForm){
		if(!Objects.equals(orderItemPojo.getQuantity(), orderItemEditForm.getQuantity())){
			return true;
		}
		return false;
	}
	private boolean isSellingPriceChange(OrderItemPojo orderItemPojo, OrderItemEditForm orderItemEditForm){
		if(!Objects.equals(orderItemPojo.getSellingPrice(), orderItemEditForm.getSellingPrice())){
			return true;
		}
		return false;
	}
	// Inundation
	private void updateProduct(OrderItemPojo orderItemPojo, OrderItemEditForm orderItemEditForm) throws ApiException {
		//INVENTORY UPDATING
		InventoryPojo inventoryPojo = inventoryService.getByProductId(orderItemPojo.getProductId());
		inventoryPojo.setQuantity(inventoryPojo.getQuantity() + orderItemPojo.getQuantity());
		inventoryService.updateByProductId(inventoryPojo.getProductId(), inventoryPojo);
		ProductPojo productPojo = productService.getByBarcode(orderItemEditForm.getBarcode());
		inventoryPojo = inventoryService.getByProductId(productPojo.getId());
		inventoryPojo.setQuantity(inventoryPojo.getQuantity() - orderItemEditForm.getQuantity());
		inventoryService.updateById(inventoryPojo.getId(),  inventoryPojo);

		//ORDERITEM UPDATION
		orderItemPojo.setProductId(productPojo.getId());
		orderItemPojo.setQuantity(orderItemEditForm.getQuantity());
		orderItemPojo.setSellingPrice(orderItemEditForm.getSellingPrice());
		orderItemService.updateById(orderItemPojo.getId(), orderItemPojo);

		//ORDER UPDATION
		updateOrderPojo(orderItemPojo);
	}

	private void updateInventory(OrderItemPojo orderItemPojo, OrderItemEditForm orderItemEditForm) throws ApiException {
		Integer initialQuantity = orderItemPojo.getQuantity();
		Integer finalQuantity = orderItemEditForm.getQuantity();
		InventoryPojo inventoryPojo = inventoryService.getByProductId(orderItemPojo.getProductId());
		if(finalQuantity < initialQuantity){
			inventoryPojo.setQuantity(inventoryPojo.getQuantity() + (initialQuantity - finalQuantity));
			inventoryService.updateById(inventoryPojo.getId(), inventoryPojo);
			orderItemPojo.setQuantity(orderItemEditForm.getQuantity());
			orderItemPojo.setSellingPrice(orderItemEditForm.getSellingPrice());
			orderItemService.updateById(orderItemPojo.getId(), orderItemPojo);
			updateOrderPojo(orderItemPojo);
		}
		else if(finalQuantity > initialQuantity){
			Integer additionalQuantityRequired = finalQuantity - initialQuantity;
			Integer quantityPresent = inventoryPojo.getQuantity();
			if(quantityPresent < additionalQuantityRequired){
				throw new ApiException("Required additional quantity is not present");
			}
			inventoryPojo.setQuantity(inventoryPojo.getQuantity() + (initialQuantity - finalQuantity));
			inventoryService.updateById(inventoryPojo.getId(), inventoryPojo);
			orderItemPojo.setQuantity(orderItemEditForm.getQuantity());
			orderItemPojo.setSellingPrice(orderItemEditForm.getSellingPrice());
			orderItemService.updateById(orderItemPojo.getId(), orderItemPojo);
			updateOrderPojo(orderItemPojo);
		}
	}

	private void updateSellingPrice(OrderItemPojo orderItemPojo, OrderItemEditForm orderItemEditForm) throws ApiException {
		orderItemPojo.setSellingPrice(orderItemEditForm.getSellingPrice());
		updateOrderPojo(orderItemPojo);
	}
	private void updateOrderPojo(OrderItemPojo orderItemPojo) throws ApiException {
		OrderPojo orderPojo = orderService.getById(orderItemPojo.getOrderId());
		orderPojo.setTime(DateTimeUtil.getZonedDateTime("Asia/Kolkata"));
		orderService.updateById(orderPojo.getId(), orderPojo);
	}
	private void checkInventory(OrderItemEditForm orderItemEditForm) throws ApiException {
		ProductPojo productPojo = productService.getByBarcode(orderItemEditForm.getBarcode());
		InventoryPojo inventoryPojo = inventoryService.getByProductId(productPojo.getId());
		Integer requestedQuantity = orderItemEditForm.getQuantity();
		Integer actualQuantity = inventoryPojo.getQuantity();
		if(requestedQuantity > actualQuantity){
			throw new ApiException("Requested quantity is currently unavailable\nQuantity available is "+actualQuantity);
		}
	}

	// Validation
	private void isBarcodeValid(OrderItemEditForm orderItemEditForm) throws ApiException {
		ProductPojo productPojo = productService.getByBarcode(orderItemEditForm.getBarcode());
	}
	private void isSellingPriceValid(OrderItemEditForm orderItemEditForm) throws ApiException {
		ProductPojo productPojo = productService.getByBarcode(orderItemEditForm.getBarcode());
		Double mrp = productPojo.getMrp();
		Double sellingPrice = orderItemEditForm.getSellingPrice();
		if(sellingPrice > mrp){
			throw new ApiException("Selling price can't be greater than MRP");
		}
	}

}
