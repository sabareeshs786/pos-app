package com.increff.posapp.dto;

import com.increff.invoiceapp.base64encoder.PdfService;
import com.increff.posapp.model.OrderData;
import com.increff.posapp.model.OrderForm;
import com.increff.posapp.model.OrderItemData;
import com.increff.posapp.model.OrderItemEditForm;
import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.*;
import com.increff.posapp.util.Converter;
import com.increff.posapp.util.DateTimeUtil;
import com.increff.posapp.util.Validator;
import org.apache.fop.apps.FOPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
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
	public List<OrderItemData> add(OrderForm form) throws ApiException, IllegalAccessException {
		Validator.orderFormValidator(form);

		OrderPojo orderPojo = new OrderPojo("Asia/Kolkata");
		orderService.add(orderPojo);

		List<OrderItemData> list = new ArrayList<>();
		Integer len = form.getBarcodes().size();
		for(int i=0; i < len; i++) {

			ProductPojo productPojo = productService.getByBarcode(form.getBarcodes().get(i));
			InventoryPojo inventoryPojo = inventoryService.getByProductId(productPojo.getId());
			Double mrp = productPojo.getMrp();
			Integer initialQuantity = inventoryPojo.getQuantity();

			validateMrp(form.getSellingPrices().get(i), mrp);
			validateQuantityRequest(form.getQuantities().get(i), initialQuantity);

			Integer finalQuantity = initialQuantity - form.getQuantities().get(i);
			inventoryPojo.setQuantity(finalQuantity);
			inventoryService.updateByProductId(inventoryPojo);

			OrderItemPojo orderItemPojo = Converter.convertToOrderItemPojo(form, i, orderPojo, inventoryPojo.getProductId());
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
	 public void convertToPdf(Integer orderId, HttpServletResponse response) throws TransformerException, FOPException, ApiException, IOException, JAXBException {
		Validator.validate("Order id", orderId);
		List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderId);
		OrderPojo orderPojo = orderService.getById(orderId);
		orderPojo.setIsInvoiced(true);
		 String date = DateTimeUtil.getDateTimeString(orderPojo.getTime(), "dd/MM/yyyy");
		 List<OrderItemData> orderItemDataList = new ArrayList<>();
		 for(OrderItemPojo orderItemPojo:orderItemPojoList){
			 ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
			 orderItemDataList.add(Converter.convertToOrderItemData(orderItemPojo, productPojo));
		 }

		 List<Integer> orderItemsIds = new ArrayList<>();
		 List<String> productNames = new ArrayList<>();
		 List<Integer> quantities = new ArrayList<>();
		 List<String> sellingPrices = new ArrayList<>();
		 List<String> mrps = new ArrayList<>();
		for(OrderItemData orderItemData: orderItemDataList){
			orderItemsIds.add(orderItemData.getId());
			productNames.add(orderItemData.getProductName());
			quantities.add(orderItemData.getQuantity());
			sellingPrices.add(orderItemData.getSellingPrice());
			mrps.add(orderItemData.getMrp());
		}

		// invoice-app is called
		String base64EncodedString = PdfService.getBase64String(
				date,
				orderId,
				orderItemsIds,
				productNames,
				quantities,
				sellingPrices,
				mrps
		);

		 byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedString);

		 response.setContentType("application/pdf");
		 response.setContentLength(decodedBytes.length);
		 response.getOutputStream().write(decodedBytes);
		 response.getOutputStream().flush();
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
		InventoryPojo inventoryPojo1 = inventoryService.getByProductId(orderItemPojo.getProductId());
		inventoryPojo1.setQuantity(inventoryPojo1.getQuantity() + orderItemPojo.getQuantity());
		inventoryService.updateByProductId(inventoryPojo1);
		ProductPojo productPojo = productService.getByBarcode(orderItemEditForm.getBarcode());
		InventoryPojo inventoryPojo2 = inventoryService.getByProductId(productPojo.getId());
		inventoryPojo2.setQuantity(inventoryPojo2.getQuantity() - orderItemEditForm.getQuantity());
		inventoryService.updateByProductId(inventoryPojo2);

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
			inventoryService.updateByProductId(inventoryPojo);
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
			inventoryService.updateByProductId(inventoryPojo);
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
