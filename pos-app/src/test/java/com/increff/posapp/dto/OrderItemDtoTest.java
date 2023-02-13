package com.increff.posapp.dto;

import com.increff.posapp.dao.*;
import com.increff.posapp.model.OrderItemData;
import com.increff.posapp.model.OrderItemEditForm;
import com.increff.posapp.pojo.*;
import com.increff.posapp.service.AbstractUnitTest;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.util.Converter;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class OrderItemDtoTest extends AbstractUnitTest {

    @Autowired
    private OrderItemDto orderItemDto;
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

    @Test
    public void testGetByOrderId() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        Integer orderId = list1.get(0).getOrderId();
        List<OrderItemData> list2 = orderItemDto.getByOrderId(orderId);
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
    public void testGetPageByOrderId() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        Integer orderId = list1.get(0).getOrderId();
        List<OrderItemData> list2 = orderItemDto.getPageByOrderId(orderId, 0, 5).getContent();
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
        OrderItemData data = orderItemDto.getByOrderItemId(list1.get(0).getId());
        assertEquals(list1.get(0).getId(), data.getId());
        assertEquals(list1.get(0).getOrderId(), data.getOrderId());
        assertEquals("product1", data.getProductName());
        assertEquals(list1.get(0).getQuantity(), data.getQuantity());
        assertEquals(list1.get(0).getSellingPrice().toString(), data.getSellingPrice());
    }
    @Test
    public void testUpdateProductChange() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        addInventory3();
        Integer id = list1.get(0).getId();
        OrderItemEditForm form = new OrderItemEditForm();
        form.setId(id);
        form.setBarcode("barcode12345");
        form.setQuantity(list1.get(0).getQuantity());
        form.setSellingPrice(list1.get(0).getSellingPrice());
        orderItemDto.update(form);
        OrderItemPojo pojo = orderItemDao.selectById(id);
        ProductPojo productPojo = productDao.selectById(pojo.getProductId());
        assertEquals("barcode12345", productPojo.getBarcode());
        assertEquals(list1.get(0).getOrderId(), pojo.getOrderId());
        assertEquals(list1.get(0).getQuantity().intValue(), pojo.getQuantity().intValue());
        assertEquals(list1.get(0).getSellingPrice(), pojo.getSellingPrice());
    }

    @Test(expected = ApiException.class)
    public void testUpdateProductQuantityChange() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        addInventory3();
        Integer id = list1.get(0).getId();
        OrderItemEditForm form = new OrderItemEditForm();
        form.setId(id);
        form.setBarcode("barcode12345");
        form.setQuantity(123);
        form.setSellingPrice(list1.get(0).getSellingPrice());
        orderItemDto.update(form);
    }

    @Test
    public void testUpdateQuantityIncrease() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        Integer id = list1.get(0).getId();
        OrderItemEditForm form = new OrderItemEditForm();
        form.setId(id);
        form.setBarcode("barcode123");
        form.setQuantity(3);
        form.setSellingPrice(list1.get(0).getSellingPrice());
        orderItemDto.update(form);
        OrderItemPojo pojo = orderItemDao.selectById(id);
        ProductPojo productPojo = productDao.selectById(pojo.getProductId());
        assertEquals("barcode123", productPojo.getBarcode());
        assertEquals(list1.get(0).getOrderId(), pojo.getOrderId());
        assertEquals(3, pojo.getQuantity().intValue());
        assertEquals(list1.get(0).getSellingPrice(), pojo.getSellingPrice());
    }

    @Test
    public void testUpdateQuantityDecrease() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        Integer id = list1.get(0).getId();
        OrderItemEditForm form = new OrderItemEditForm();
        form.setId(id);
        form.setBarcode("barcode123");
        form.setQuantity(1);
        form.setSellingPrice(list1.get(0).getSellingPrice());
        orderItemDto.update(form);
        OrderItemPojo pojo = orderItemDao.selectById(id);
        ProductPojo productPojo = productDao.selectById(pojo.getProductId());
        assertEquals("barcode123", productPojo.getBarcode());
        assertEquals(list1.get(0).getOrderId(), pojo.getOrderId());
        assertEquals(1, pojo.getQuantity().intValue());
        assertEquals(list1.get(0).getSellingPrice(), pojo.getSellingPrice());
    }
    @Test(expected = ApiException.class)
    public void testUpdateQuantityInvalid() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        Integer id = list1.get(0).getId();
        OrderItemEditForm form = new OrderItemEditForm();
        form.setId(id);
        form.setBarcode("barcode123");
        form.setQuantity(300);
        form.setSellingPrice(list1.get(0).getSellingPrice());
        orderItemDto.update(form);
    }

    @Test
    public void testUpdateSellingPriceChange() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        Integer id = list1.get(0).getId();
        OrderItemEditForm form = new OrderItemEditForm();
        form.setId(id);
        form.setBarcode("barcode123");
        form.setQuantity(list1.get(0).getQuantity());
        form.setSellingPrice(110.78);
        orderItemDto.update(form);
        OrderItemPojo pojo = orderItemDao.selectById(id);
        ProductPojo productPojo = productDao.selectById(pojo.getProductId());
        assertEquals("barcode123", productPojo.getBarcode());
        assertEquals(list1.get(0).getOrderId(), pojo.getOrderId());
        assertEquals(list1.get(0).getQuantity().intValue(), pojo.getQuantity().intValue());
        assertEquals("110.78", pojo.getSellingPrice().toString());
    }

    @Test(expected = ApiException.class)
    public void testUpdateSellingPriceInvalid() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        Integer id = list1.get(0).getId();
        OrderItemEditForm form = new OrderItemEditForm();
        form.setId(id);
        form.setBarcode("barcode123");
        form.setQuantity(list1.get(0).getQuantity());
        form.setSellingPrice(11000.78);
        orderItemDto.update(form);
    }

    @Test
    public void testUpdateNoUpdate() throws ApiException {
        List<OrderItemPojo> list1 = addOrderItems();
        Integer id = list1.get(0).getId();
        OrderItemEditForm form = new OrderItemEditForm();
        form.setId(id);
        form.setBarcode("barcode123");
        form.setQuantity(list1.get(0).getQuantity());
        form.setSellingPrice(list1.get(0).getSellingPrice());
        orderItemDto.update(form);
        OrderItemPojo pojo = orderItemDao.selectById(id);
        ProductPojo productPojo = productDao.selectById(pojo.getProductId());
        assertEquals("barcode123", productPojo.getBarcode());
        assertEquals(list1.get(0).getOrderId(), pojo.getOrderId());
        assertEquals(list1.get(0).getQuantity().intValue(), pojo.getQuantity().intValue());
        assertEquals(list1.get(0).getSellingPrice().toString(), pojo.getSellingPrice().toString());
    }
}
