package com.increff.posapp.dto;

import com.increff.invoiceapp.base64encoder.PdfService;
import com.increff.posapp.model.*;
import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.*;
import com.increff.posapp.util.Converter;
import com.increff.posapp.util.DateTimeUtil;
import com.increff.posapp.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;

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
	public List<OrderItemData> add(OrderForm form) throws ApiException{
		Validator.orderFormValidator(form);

		OrderPojo orderPojo = new OrderPojo("Asia/Kolkata");
		orderService.add(orderPojo);


		return addItems(orderPojo, form);
	}

	private List<OrderItemData> addItems(OrderPojo orderPojo, OrderForm form) throws ApiException {
		List<OrderItemData> list = new ArrayList<>();
		Integer len = form.getBarcodes().size();
		for(int i=0; i < len; i++) {

			ProductPojo productPojo = productService.getByBarcode(form.getBarcodes().get(i));
			InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());
			Double mrp = productPojo.getMrp();
			Integer initialQuantity = inventoryPojo.getQuantity();

			validateMrp(form.getSellingPrices().get(i), mrp);
			validateQuantityRequest(form.getQuantities().get(i), initialQuantity);

			Integer finalQuantity = initialQuantity - form.getQuantities().get(i);
			inventoryPojo.setQuantity(finalQuantity);
			inventoryService.update(inventoryPojo);

			OrderItemPojo itemPojo = orderItemService.getOrderIdAndProductId(
					orderPojo.getId(),
					productPojo.getId());
			if(itemPojo != null){
				itemPojo.setQuantity(itemPojo.getQuantity() + form.getQuantities().get(i));
				itemPojo.setSellingPrice(form.getSellingPrices().get(i));
				orderItemService.updateById(itemPojo.getId(), itemPojo);
				continue;
			}

			OrderItemPojo orderItemPojo = Converter.convertToOrderItemPojo(
					form, i, orderPojo, inventoryPojo.getProductId());
			list.add(
					Converter.convertToOrderItemData(
							orderItemService.add(orderItemPojo),
							productPojo)
			);
		}
		return list;
	}
	public List<OrderData> getAll() throws ApiException {
		List<OrderPojo> orderPojoList = orderService.getAll();
		Map<Integer, List<OrderItemPojo>> integerListMap = new HashMap<>();
		for(OrderPojo pojo: orderPojoList){
			integerListMap.put(pojo.getId(), orderItemService.getByOrderId(pojo.getId()));
		}
		return Converter.convertToOrderDataList(orderPojoList, integerListMap);
	}

	public Page<OrderData> getAll(Integer page, Integer size) throws ApiException {
		Validator.validate("Page", page);
		Validator.validate("Size", size);
		List<OrderPojo> orderPojoList = orderService.getAll(page, size);
		Map<Integer, List<OrderItemPojo>> integerListMap = new HashMap<>();
		for(OrderPojo pojo: orderPojoList){
			integerListMap.put(pojo.getId(), orderItemService.getByOrderId(pojo.getId()));
		}
		List<OrderData> orderDataList =Converter.convertToOrderDataList(orderPojoList, integerListMap);
		return new PageImpl<>(orderDataList, PageRequest.of(page, size), orderService.getTotalElements());
	}

	 @Transactional(rollbackOn = ApiException.class)
	 public void convertToPdf(Integer orderId, HttpServletResponse response) throws ApiException{
		try{
			Validator.validate("Order id", orderId);
			List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderId);
			OrderPojo orderPojo = orderService.getById(orderId);
			orderPojo.setOrderStatus(OrderStatus.INVOICED);
			String date = DateTimeUtil.getDateTimeString(orderPojo.getTime(), "dd/MM/yyyy");
			List<OrderItemData> orderItemDataList = new ArrayList<>();
			for(OrderItemPojo orderItemPojo:orderItemPojoList){
				ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
				orderItemDataList.add(Converter.convertToOrderItemData(orderItemPojo, productPojo));
			}

			List<Integer> snos = new ArrayList<>();
			List<String> productNames = new ArrayList<>();
			List<Integer> quantities = new ArrayList<>();
			List<String> mrps = new ArrayList<>();
			List<String> sellingPrices = new ArrayList<>();
			int sno = 0;
			for(OrderItemData orderItemData: orderItemDataList){
				sno += 1;
				snos.add(sno);
				productNames.add(orderItemData.getProductName());
				quantities.add(orderItemData.getQuantity());
				sellingPrices.add(orderItemData.getSellingPrice());
				mrps.add(orderItemData.getMrp());
			}

			// invoice-app is called
			String base64EncodedString = PdfService.getBase64String(
					date,
					orderId,
					snos,
					productNames,
					quantities,
					mrps,
					sellingPrices
			);

			byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedString);

			response.setContentType("application/pdf");
			response.setContentLength(decodedBytes.length);
			response.getOutputStream().write(decodedBytes);
			response.getOutputStream().flush();
		}
		catch (Exception ex){
			throw new ApiException("Cannot generate invoice pdf");
		}
	 }

	public List<OrderItemData> getByOrderId(Integer orderId) throws ApiException {
		Validator.validate("Order id", orderId);
		List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderId);
		List<ProductPojo> productPojoList = new ArrayList<>();
		for(OrderItemPojo p: orderItemPojoList){
			productPojoList.add(productService.getById(p.getProductId()));
		}
		return Converter.convertToOrderItemDataList(orderItemPojoList, productPojoList);
	}

	public Page<OrderItemData> getPageByOrderId(Integer orderId, Integer page, Integer size) throws ApiException {
		Validator.validate("Order id", orderId);
		Validator.validate("Page", page);
		Validator.validate("Size", size);
		List<OrderItemPojo> orderItemPojoList = orderItemService.getPageByOrderId(orderId, page, size);
		List<ProductPojo> productPojoList = new ArrayList<>();
		for(OrderItemPojo pojo: orderItemPojoList){
			productPojoList.add(productService.getById(pojo.getProductId()));
		}
		List<OrderItemData> list = Converter.convertToOrderItemDataList(orderItemPojoList, productPojoList);
		return new PageImpl<>(
				list,
				PageRequest.of(page, size),
				orderItemService.getByOrderIdTotalElements(orderId)
		);
	}

	public OrderItemData getByOrderItemId(Integer id) throws ApiException {
		Validator.validate("Id", id);
		OrderItemPojo orderItemPojo = orderItemService.getById(id);
		ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
		return Converter.convertToOrderItemData(orderItemPojo, productPojo);
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

	// Edit order item


	public void update(Integer id, OrderItemEditForm orderItemEditForm) throws ApiException, IllegalAccessException {
		Validator.validate(orderItemEditForm);
		OrderItemPojo orderItemPojo = orderItemService.getById(id);
		isInvoiced(orderService.getById(orderItemPojo.getOrderId()));
		checkInventory(orderItemEditForm, orderItemPojo);
		isSellingPriceValid(orderItemEditForm);
		updateInventory(orderItemEditForm, orderItemPojo);
		updateSellingPrice(orderItemEditForm, orderItemPojo);
		orderItemService.updateById(id, orderItemPojo);
		updateOrderPojo(orderItemPojo);
	}

	// Updating methods
	private void updateInventory(OrderItemEditForm orderItemEditForm, OrderItemPojo orderItemPojo) throws ApiException {
		Integer initialQuantity = orderItemPojo.getQuantity();
		Integer finalQuantity = orderItemEditForm.getQuantity();
		InventoryPojo inventoryPojo = inventoryService.get(orderItemPojo.getProductId());

		if(finalQuantity < initialQuantity){
			inventoryPojo.setQuantity(inventoryPojo.getQuantity() + (initialQuantity - finalQuantity));
			inventoryService.update(inventoryPojo);
			orderItemPojo.setQuantity(orderItemEditForm.getQuantity());
			orderItemPojo.setSellingPrice(orderItemEditForm.getSellingPrice());
			orderItemService.updateById(orderItemPojo.getId(), orderItemPojo);
		}
		else if(finalQuantity > initialQuantity){
			Integer additionalQuantityRequired = finalQuantity - initialQuantity;
			Integer quantityPresent = inventoryPojo.getQuantity();
			if(quantityPresent < additionalQuantityRequired){
				throw new ApiException("Required additional quantity is not present");
			}
			inventoryPojo.setQuantity(inventoryPojo.getQuantity() + (initialQuantity - finalQuantity));
			inventoryService.update(inventoryPojo);
			orderItemPojo.setQuantity(orderItemEditForm.getQuantity());
			orderItemPojo.setSellingPrice(orderItemEditForm.getSellingPrice());
			orderItemService.updateById(orderItemPojo.getId(), orderItemPojo);
		}
	}

	private void updateSellingPrice(OrderItemEditForm orderItemEditForm, OrderItemPojo orderItemPojo) throws ApiException {
		orderItemPojo.setSellingPrice(orderItemEditForm.getSellingPrice());
	}
	private void updateOrderPojo(OrderItemPojo orderItemPojo) throws ApiException {
		OrderPojo orderPojo = orderService.getById(orderItemPojo.getOrderId());
		orderPojo.setTime(DateTimeUtil.getZonedDateTime("Asia/Kolkata"));
		orderService.updateById(orderPojo.getId(), orderPojo);
	}


	// Validating methods
	private void checkInventory(OrderItemEditForm orderItemEditForm, OrderItemPojo orderItemPojo) throws ApiException {
		ProductPojo productPojo = productService.getByBarcode(orderItemEditForm.getBarcode());
		InventoryPojo inventoryPojo = inventoryService.get(productPojo.getId());
		Integer additionalQuantity = orderItemEditForm.getQuantity() - orderItemPojo.getQuantity();
		Integer actualQuantity = inventoryPojo.getQuantity();
		if(additionalQuantity > actualQuantity){
			throw new ApiException("Requested quantity is currently unavailable\nQuantity available is "+actualQuantity);
		}
	}

	// Validation
	private void isSellingPriceValid(OrderItemEditForm orderItemEditForm) throws ApiException {
		ProductPojo productPojo = productService.getByBarcode(orderItemEditForm.getBarcode());
		Double mrp = productPojo.getMrp();
		Double sellingPrice = orderItemEditForm.getSellingPrice();
		if(sellingPrice > mrp){
			throw new ApiException("Selling price can't be greater than MRP");
		}
	}

	public void addNewItems(Integer id, OrderForm form) throws ApiException {
		OrderPojo orderPojo = orderService.getById(id);
		addItems(orderPojo, form);
		orderPojo.setOrderStatus(OrderStatus.NOT_INVOICED);
		orderPojo.setTime(DateTimeUtil.getZonedDateTime("Asia/Kolkata"));
		orderService.updateById(id, orderPojo);
	}

	private void isInvoiced(OrderPojo pojo) throws ApiException {
		if(pojo.getOrderStatus().equals(OrderStatus.INVOICED)){
			throw new ApiException("Invoiced orders can't be edited");
		}
	}

	public void deleteOrderItem(Integer id) throws ApiException {
		OrderItemPojo orderItemPojo = orderItemService.getById(id);
		InventoryPojo inventoryPojo = inventoryService.get(orderItemPojo.getProductId());
		inventoryPojo.setQuantity(inventoryPojo.getQuantity() + orderItemPojo.getQuantity());
		inventoryService.update(inventoryPojo);
		orderItemService.deleteById(id);
	}

}
