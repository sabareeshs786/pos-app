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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import org.springframework.util.Base64Utils;

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
	private static final Logger logger = Logger.getLogger(OrderDto.class);

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

			OrderItemPojo orderItemPojo = Converter.convertToOrderItemPojo(form, i, orderPojo, productId);
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
		Page<OrderData> dataPage = new PageImpl<>(list2, PageRequest.of(page, size), pojoPage.getTotalElements());
		return dataPage;
	}
	 public void convertToPdf(Integer orderId, HttpServletResponse response) throws TransformerException, FOPException, ApiException, IOException {
		 List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderId);
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
		String base64EncodedString = PdfService.getBase64String(
				orderItemsIds,
				productNames,
				quantities,
				sellingPrices,
				mrps
		);

		 logger.info("Got encoded string");
		 logger.info("Encoded string: "+base64EncodedString);
		 byte[] decodedBytes = Base64Utils.decodeFromString(base64EncodedString);
		 logger.info("Decoded bytes: "+decodedBytes.toString());
		 assert decodedBytes != null;

//		 ByteArrayInputStream inputStream = new ByteArrayInputStream(base64EncodedString.getBytes());
		 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		 FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
		 Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, outputStream);

		 TransformerFactory factory = TransformerFactory.newInstance();
		 Transformer transformer = factory.newTransformer(new StreamSource(new File("invoice.xsl")));
		 transformer.transform(new StreamSource(new File("invoice.xml")), new SAXResult(fop.getDefaultHandler()));

//		 byte[] pdfBytes = outputStream.toByteArray();
//		 HttpHeaders headers = new HttpHeaders();
//		 headers.setContentType(MediaType.APPLICATION_PDF);
//		 headers.setContentLength(pdfBytes.length);
//		 headers.setContentDispositionFormData("attachment", "invoice.pdf");
		 String pdfFileName = "output.pdf";
		 response.reset();
		 response.addHeader("Pragma", "public");
		 response.addHeader("Cache-Control", "max-age=0");
		 response.setHeader("Content-disposition", "attachment;filename=" + pdfFileName);
		 response.setContentType("application/pdf");

		 // avoid "byte shaving" by specifying precise length of transferred data
		 response.setContentLength(outputStream.size());
		 ServletOutputStream servletOutputStream = response.getOutputStream();
		 servletOutputStream.write(outputStream.toByteArray());
		 servletOutputStream.flush();
		 servletOutputStream.close();
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
