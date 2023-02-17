package com.increff.posapp.dto;

import com.increff.posapp.dao.*;
import com.increff.posapp.model.OrderData;
import com.increff.posapp.model.OrderForm;
import com.increff.posapp.model.OrderItemData;
import com.increff.posapp.model.OrderItemEditForm;
import com.increff.posapp.pojo.*;
import com.increff.posapp.service.AbstractUnitTest;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.util.Converter;
import org.apache.fop.apps.FOPException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderDto orderDto;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    private BrandPojo addBrand1(){
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand1");
        pojo.setCategory("category1");
        return brandDao.insert(pojo);
    }
    private BrandPojo addBrand2(){
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand2");
        pojo.setCategory("category2");
        return brandDao.insert(pojo);
    }
    private BrandPojo addBrand3(){
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand3");
        pojo.setCategory("category3");
        return brandDao.insert(pojo);
    }
    private ProductPojo addProduct1() throws ApiException {
        BrandPojo brandPojo = addBrand1();
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode123");
        pojo.setBrandCategory(brandPojo.getId());
        pojo.setName("product1");
        pojo.setMrp(123.45);
        return productDao.insert(pojo);
    }
    private ProductPojo addProduct2() throws ApiException {
        BrandPojo brandPojo = addBrand2();
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode1234");
        pojo.setBrandCategory(brandPojo.getId());
        pojo.setName("product2");
        pojo.setMrp(12.45);
        return productDao.insert(pojo);
    }
    private ProductPojo addProduct3() throws ApiException {
        BrandPojo brandPojo = addBrand3();
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode12345");
        pojo.setBrandCategory(brandPojo.getId());
        pojo.setName("product3");
        pojo.setMrp(1234.56);
        return productDao.insert(pojo);
    }

    private InventoryPojo addInventory1() throws ApiException {
        ProductPojo pojo = addProduct1();
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(pojo.getId());
        inventoryPojo.setQuantity(21);
        return inventoryDao.add(inventoryPojo);
    }

    private InventoryPojo addInventory2() throws ApiException {
        ProductPojo pojo = addProduct2();
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(pojo.getId());
        inventoryPojo.setQuantity(23);
        return inventoryDao.add(inventoryPojo);
    }
    private InventoryPojo addInventory3() throws ApiException {
        ProductPojo pojo = addProduct3();
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(pojo.getId());
        inventoryPojo.setQuantity(26);
        return inventoryDao.add(inventoryPojo);
    }

    private List<OrderItemPojo> addOrderItems() throws ApiException {

        List<OrderItemPojo> list = new ArrayList<>();

        InventoryPojo inventoryPojo1 = addInventory1();
        InventoryPojo inventoryPojo2 = addInventory2();
        OrderPojo pojo = new OrderPojo("Asia/Kolkata");
        OrderPojo orderPojo = orderDao.insert(pojo);
        OrderItemPojo orderItemPojo1 = new OrderItemPojo();
        orderItemPojo1.setOrderId(orderPojo.getId());
        orderItemPojo1.setProductId(inventoryPojo1.getProductId());
        orderItemPojo1.setQuantity(2);
        orderItemPojo1.setSellingPrice(120.68);
        list.add(orderItemDao.insert(orderItemPojo1));

        OrderItemPojo orderItemPojo2 = new OrderItemPojo();
        orderItemPojo2.setOrderId(orderPojo.getId());
        orderItemPojo2.setProductId(inventoryPojo2.getProductId());
        orderItemPojo2.setQuantity(2);
        orderItemPojo2.setSellingPrice(10.78);
        list.add(orderItemDao.insert(orderItemPojo2));

        return list;
    }

    private List<OrderItemData> addOrder() throws ApiException {
        addInventory1();
        addInventory2();
        OrderForm form = new OrderForm();
        form.getBarcodes().add("barcode123");
        form.getQuantities().add(2);
        form.getSellingPrices().add(120.68);
        form.getBarcodes().add("barcode1234");
        form.getQuantities().add(2);
        form.getSellingPrices().add(10.78);
        return orderDto.add(form);
    }

    @Test
    public void testGetByOrderId() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        Integer orderId = list1.get(0).getOrderId();
        List<OrderItemData> list2 = orderDto.getByOrderId(orderId);
        assertEquals(list1.size(), list2.size());
        for(int i=0; i < list1.size(); i++){
            assertEquals(list1.get(i).getOrderId(), list2.get(i).getOrderId());
            assertEquals(list1.get(i).getQuantity(), list2.get(i).getQuantity());
            assertEquals(list1.get(i).getSellingPrice().toString(), list2.get(i).getSellingPrice());
        }
        assertEquals("product1", list2.get(0).getProductName());
        assertEquals("product2", list2.get(1).getProductName());
        assertNotEquals(list1.get(0).getProductId(), list1.get(1).getProductId());
    }

    @Test
    public void testAdd() throws ApiException {
        InventoryPojo inventoryPojo1 = addInventory1();
        InventoryPojo inventoryPojo2 = addInventory2();
        OrderForm form = new OrderForm();
        form.getBarcodes().add("barcode123");
        form.getQuantities().add(2);
        form.getSellingPrices().add(120.68);
        form.getBarcodes().add("barcode1234");
        form.getQuantities().add(2);
        form.getSellingPrices().add(10.78);
        List<OrderItemData> list = orderDto.add(form);
        assertEquals("barcode123", list.get(0).getBarcode());
        assertEquals("product1", list.get(0).getProductName());
        assertEquals(2, list.get(0).getQuantity().intValue());
        assertEquals("120.68", list.get(0).getSellingPrice());
        assertEquals("123.45", list.get(0).getMrp());

        assertEquals("barcode1234", list.get(1).getBarcode());
        assertEquals("product2", list.get(1).getProductName());
        assertEquals(2, list.get(1).getQuantity().intValue());
        assertEquals("10.78", list.get(1).getSellingPrice());
        assertEquals("12.45", list.get(1).getMrp());

        assertNotNull(list.get(0).getOrderId());
        assertNotNull(list.get(1).getOrderId());
        assertEquals(list.get(0).getOrderId(), list.get(1).getOrderId());
        assertEquals(19, inventoryPojo1.getQuantity().intValue());
        assertEquals(21, inventoryPojo2.getQuantity().intValue());
    }

    @Test
    public void testGetAll() throws ApiException {
        List<OrderItemData> list = addOrder();
        List<OrderData> orderDataList = orderDto.getAll();
        assertEquals(2, list.size());
        assertEquals(1, orderDataList.size());
        assertEquals(
                LocalDate.now(
                        ZoneId.of("Asia/Kolkata")
                ).format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                ),
                orderDataList.get(0).getTime().substring(0, 10)
        );
        assertEquals("262.92",orderDataList.get(0).getTotalAmount());
    }

    @Test
    public void testGetAllPage() throws ApiException {
        List<OrderItemData> list = addOrder();
        List<OrderData> orderDataList = orderDto.getAll(0, 5).getContent();
        assertEquals(2, list.size());
        assertEquals(1, orderDataList.size());
        assertTrue(orderDataList.size() > 0 && orderDataList.size() <= 5);
        assertEquals(
                LocalDate.now(
                        ZoneId.of("Asia/Kolkata")
                ).format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                ),
                orderDataList.get(0).getTime().substring(0, 10)
        );
        assertEquals("262.92",orderDataList.get(0).getTotalAmount());
    }

    @Test
    public void testConvertToPdf() throws ApiException, FOPException, JAXBException, IOException, TransformerException {
        HttpServletResponse response = new MockHttpServletResponse();
        List<OrderItemData> list = addOrder();
        assertEquals(2,list.size());
        orderDto.convertToPdf(list.get(0).getOrderId(), response);
        assertEquals("application/pdf", response.getContentType());
        assertTrue(response.getBufferSize() > 0);
    }

    @Test(expected = ApiException.class)
    public void testValidateMrpInvalid() throws ApiException {
        addInventory1();
        addInventory2();
        OrderForm form = new OrderForm();
        form.getBarcodes().add("barcode123");
        form.getQuantities().add(200);
        form.getSellingPrices().add(1200.78);
        form.getBarcodes().add("barcode1234");
        form.getQuantities().add(29);
        form.getSellingPrices().add(1008.7);
        orderDto.add(form);
    }

    @Test(expected = ApiException.class)
    public void testValidateQuantityInvalid() throws ApiException {
        addInventory1();
        addInventory2();
        OrderForm form = new OrderForm();
        form.getBarcodes().add("barcode123");
        form.getQuantities().add(290);
        form.getSellingPrices().add(120.68);
        form.getBarcodes().add("barcode1234");
        form.getQuantities().add(287);
        form.getSellingPrices().add(10.78);
        orderDto.add(form);
    }
    @Test
    public void testGetPageByOrderId() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        Integer orderId = list1.get(0).getOrderId();
        List<OrderItemData> list2 = orderDto.getPageByOrderId(orderId, 0, 5).getContent();
        assertEquals(list1.size(), list2.size());
        assertTrue(list2.size() > 0 && list2.size() <= 5);
        for(int i=0; i < list1.size(); i++){
            assertEquals(list1.get(i).getOrderId(), list2.get(i).getOrderId());
            assertEquals(list1.get(i).getQuantity(), list2.get(i).getQuantity());
            assertEquals(list1.get(i).getSellingPrice().toString(), list2.get(i).getSellingPrice());
        }
        assertEquals("product1", list2.get(0).getProductName());
        assertEquals("product2", list2.get(1).getProductName());
        assertNotEquals(list1.get(0).getProductId(), list1.get(1).getProductId());
    }

    @Test
    public void testGetByOrderItemId() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        OrderItemData data = orderDto.getByOrderItemId(list1.get(0).getId());
        assertEquals(list1.get(0).getId(), data.getId());
        assertEquals(list1.get(0).getOrderId(), data.getOrderId());
        assertEquals("product1", data.getProductName());
        assertEquals(list1.get(0).getQuantity(), data.getQuantity());
        assertEquals(list1.get(0).getSellingPrice().toString(), data.getSellingPrice());
    }

}