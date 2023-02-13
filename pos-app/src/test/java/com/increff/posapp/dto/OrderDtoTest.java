package com.increff.posapp.dto;

import com.increff.posapp.dao.BrandDao;
import com.increff.posapp.dao.InventoryDao;
import com.increff.posapp.dao.OrderDao;
import com.increff.posapp.dao.ProductDao;
import com.increff.posapp.model.*;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.AbstractUnitTest;
import com.increff.posapp.service.ApiException;
import org.apache.fop.apps.FOPException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;

public class OrderDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderDto orderDto;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private InventoryDao inventoryDao;
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
}
