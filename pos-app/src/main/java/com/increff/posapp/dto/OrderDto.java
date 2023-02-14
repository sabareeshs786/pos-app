package com.increff.posapp.dto;

import com.increff.invoiceapp.base64encoder.PdfService;
import com.increff.posapp.model.OrderData;
import com.increff.posapp.model.OrderForm;
import com.increff.posapp.model.OrderItemData;
import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.*;
import com.increff.posapp.util.Converter;
import com.increff.posapp.util.DateTimeUtil;
import com.increff.posapp.util.FormValidator;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Base64;

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
	private static final Logger logger = Logger.getLogger(OrderDto.class);
	private static final String RESOURCES_DIR = System.getProperty("user.dir") + "/src/main/resources/com/increff/posapp";


	@Transactional(rollbackOn = ApiException.class)
	public List<OrderItemData> add(OrderForm form) throws ApiException {
		List<OrderItemData> list = new ArrayList<>();
		OrderPojo orderPojo = new OrderPojo("Asia/Kolkata");
		orderService.add(orderPojo);
		Integer len = form.getBarcodes().size();
		FormValidator.orderFormValidator(form);
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
		List<OrderPojo> list = orderService.getAll();
		List<OrderData> list2 = new ArrayList<OrderData>();
		Double totalAmount = 0.0;
		for (OrderPojo orderPojo : list) {
			totalAmount = 0.00;
			for(OrderItemPojo orderItemPojo: orderItemService.getByOrderId(orderPojo.getId())){
				totalAmount += orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity();
			}
			list2.add(Converter.convertToOrderData(orderPojo, totalAmount));
		}
		return list2;
	}

	public Page<OrderData> getAll(Integer page, Integer size) throws ApiException {
		Page<OrderPojo> pojoPage = orderService.getAllByPage(page, size);
		List<OrderPojo> list = pojoPage.getContent();
		List<OrderData> list2 = new ArrayList<OrderData>();
		Double totalAmount = 0.0;
		for (OrderPojo orderPojo : list) {
			totalAmount = 0.00;
			for(OrderItemPojo orderItemPojo: orderItemService.getByOrderId(orderPojo.getId())){
				totalAmount += orderItemPojo.getSellingPrice() * orderItemPojo.getQuantity();
			}
			list2.add(Converter.convertToOrderData(orderPojo, totalAmount));
		}
		return new PageImpl<>(list2, PageRequest.of(page, size), pojoPage.getTotalElements());
	}

	 @Transactional(rollbackOn = ApiException.class)
	 public void convertToPdf(Integer orderId, HttpServletResponse response) throws TransformerException, FOPException, ApiException, IOException, JAXBException {
		 List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderId);
		 OrderPojo orderPojo = orderService.getById(orderId);
		 String date = DateTimeUtil.getDateTimeString(orderPojo.getTime(), "dd/MM/yyyy");
		 List<OrderItemData> orderItemDataList = new ArrayList<>();
		 for(OrderItemPojo orderItemPojo:orderItemPojoList){
			 ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
			 orderItemDataList.add(Converter.convertToOrderItemData(orderItemPojo, productPojo));
		 }

		 logger.info("OrderItemPojoList Size: "+orderItemPojoList.size());

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
		logger.info("Fields created");

		// invoice-app is called
		String base64EncodedString = PdfService.getBase64String(
				date,
				orderItemsIds,
				productNames,
				quantities,
				sellingPrices,
				mrps
		);

		 byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedString);

		 //Prepare response
		 response.setContentType("application/pdf");
		 response.setContentLength(decodedBytes.length);

		 //Send content to Browser
		 response.getOutputStream().write(decodedBytes);
		 response.getOutputStream().flush();
		 logger.info("PDF generated");
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
