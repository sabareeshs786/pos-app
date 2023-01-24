package com.increff.posapp.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.increff.posapp.model.*;

import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.ApiException;

public class ConverterDto {
	public static BrandData convertToBrandData(BrandPojo p) {
		BrandData d = new BrandData();
		d.setBrand(p.getBrand());
		d.setCategory(p.getCategory());
		d.setId(p.getId());
		return d;
	}

	public static BrandPojo convertToBrandPojo(BrandForm f) {
		BrandPojo p = new BrandPojo();
		p.setBrand(f.getBrand());
		p.setCategory(f.getCategory());
		return p;
	}
	
	public static ProductData convertToProductData(ProductPojo productPojo, BrandPojo brandPojo) {
		ProductData productData = new ProductData();
		productData.setId(productPojo.getId());
		productData.setBarcode(productPojo.getBarcode());
		productData.setBrand(brandPojo.getBrand());
		productData.setCategory(brandPojo.getCategory());
		productData.setBrand_category(productPojo.getBrandCategory());
		productData.setName(productPojo.getName());
		productData.setMrp(DoubleUtil.roundToString(productPojo.getMrp()));
		return productData;
	}

	public static ProductPojo convertToProductPojo(ProductForm f, Integer brandCategory) {
		ProductPojo p = new ProductPojo();
		p.setBarcode(f.getBarcode());
		p.setBrandCategory(brandCategory);
		System.out.println(f.getMrp());
		p.setMrp(DoubleUtil.round(Double.parseDouble(f.getMrp()), 2));
		p.setName(f.getName());
		return p;
	}
	
	public static InventoryPojo convertToInventoryPojo(InventoryForm f, Integer productId) throws ApiException{
		InventoryPojo p = new InventoryPojo();
		p.setProductId(productId);
		p.setQuantity(f.getQuantity());
		return p;
	}
	
	public static InventoryData convertToInventoryData(InventoryPojo p, String barcode) {
		InventoryData d = new InventoryData();
		d.setId(p.getId());
		d.setBarcode(barcode);
		d.setQuantity(p.getQuantity());
		return d;
	}
	
	public static OrderData convertToOrderData(OrderPojo orderPojo, Double totalAmount) {
		OrderData orderData = new OrderData();
		orderData.setId(orderPojo.getId());
		String format = "MM/dd/yyyy - HH:mm:ss";
		orderData.setTime(DateTimeUtil.getDateTime(orderPojo.getTime(), format));
		orderData.setTotalAmount(DoubleUtil.roundToString(totalAmount));
		return orderData;
	}
	
	public static OrderItemPojo convertToOrderItemPojo(OrderForm form,  OrderPojo orderPojo, Integer productId) throws ApiException {
		OrderItemPojo orderItemPojo = new OrderItemPojo();
		orderItemPojo.setOrderId(orderPojo.getId());
		orderItemPojo.setProductId(productId);
		orderItemPojo.setQuantity(form.getQuantity());
		orderItemPojo.setSellingPrice(form.getSellingPrice());
		return orderItemPojo;
	}
	public static OrderPojo convertToOrderPojo() throws ApiException{
		OrderPojo p = new OrderPojo();
		LocalDateTime localDateTime = LocalDateTime.now();
		ZoneId india = ZoneId.of("Asia/Kolkata");
		ZonedDateTime zone1 = ZonedDateTime.of(localDateTime, india);
		p.setTime(zone1);
		return p;
	}

	public static OrderItemsData convertToOrderItemsData(OrderItemPojo orderItemPojo, ProductPojo productPojo){
		OrderItemsData orderItemsData = new OrderItemsData();
		orderItemsData.setId(orderItemPojo.getId());
		orderItemsData.setOrderId(orderItemPojo.getOrderId());
		orderItemsData.setProductName(productPojo.getName());
		orderItemsData.setQuantity(orderItemPojo.getQuantity());
		orderItemsData.setSellingPrice(orderItemPojo.getSellingPrice());
		orderItemsData.setMrp(productPojo.getMrp());
		return orderItemsData;
	}

	public static OrderItemData convertToOrderItemData(OrderItemPojo orderItemPojo, ProductPojo productPojo){
		OrderItemData orderItemData = new OrderItemData();
		orderItemData.setId(orderItemPojo.getId());
		orderItemData.setOrderId(orderItemPojo.getOrderId());
		orderItemData.setQuantity(orderItemPojo.getQuantity());
		orderItemData.setBarcode(productPojo.getBarcode());
		orderItemData.setProductName(productPojo.getName());
		orderItemData.setSellingPrice(orderItemPojo.getSellingPrice());
		orderItemData.setMrp(productPojo.getMrp());
		return orderItemData;

	}

	public static InventoryReportData convertToInventoryReportData(InventoryData inventoryData, BrandPojo brandPojo){
		InventoryReportData inventoryReportData = new InventoryReportData();
		inventoryReportData.setBrand(brandPojo.getBrand());
		inventoryReportData.setCategory(brandPojo.getCategory());
		inventoryReportData.setId(inventoryData.getId());
		inventoryReportData.setQuantity(inventoryData.getQuantity());
		inventoryReportData.setBarcode(inventoryData.getBarcode());
		return inventoryReportData;
	}

	public static SalesReportData convertToSalesReportData(SalesReportData salesReportData, String key, Double value, Integer quantity){
		String[] brandCategoryArr = key.split("--");
		salesReportData.getBrands().add(brandCategoryArr[0]);
		salesReportData.getCategories().add(brandCategoryArr[1]);
		salesReportData.getQuantities().add(quantity);
		salesReportData.getTotalAmounts().add(DoubleUtil.roundToString(value));
		return null;
	}
}
