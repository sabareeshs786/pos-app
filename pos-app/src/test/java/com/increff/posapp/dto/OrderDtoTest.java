//package com.increff.posapp.dto;
//
//import com.increff.posapp.dao.BrandDao;
//import com.increff.posapp.dao.InventoryDao;
//import com.increff.posapp.dao.OrderDao;
//import com.increff.posapp.dao.ProductDao;
//import com.increff.posapp.model.*;
//import com.increff.posapp.pojo.BrandPojo;
//import com.increff.posapp.pojo.InventoryPojo;
//import com.increff.posapp.pojo.ProductPojo;
//import com.increff.posapp.service.AbstractUnitTest;
//import com.increff.posapp.service.ApiException;
//import org.apache.fop.apps.FOPException;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mock.web.MockHttpServletResponse;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.xml.bind.JAXBException;
//import javax.xml.transform.TransformerException;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class OrderDtoTest extends AbstractUnitTest {
//
//    @Autowired
//    private OrderDto orderDto;
//    @Autowired
//    private BrandDao brandDao;
//    @Autowired
//    private ProductDao productDao;
//    @Autowired
//    private InventoryDao inventoryDao;
//    private BrandPojo addBrand1(){
//        BrandPojo pojo = new BrandPojo();
//        pojo.setBrand("brand1");
//        pojo.setCategory("category1");
//        return brandDao.insert(pojo);
//    }
//    private BrandPojo addBrand2(){
//        BrandPojo pojo = new BrandPojo();
//        pojo.setBrand("brand2");
//        pojo.setCategory("category2");
//        return brandDao.insert(pojo);
//    }
//    private ProductPojo addProduct1() throws ApiException {
//        BrandPojo brandPojo = addBrand1();
//        ProductPojo pojo = new ProductPojo();
//        pojo.setBarcode("barcode123");
//        pojo.setBrandCategory(brandPojo.getId());
//        pojo.setName("product1");
//        pojo.setMrp(123.45);
//        return productDao.insert(pojo);
//    }
//    private ProductPojo addProduct2() throws ApiException {
//        BrandPojo brandPojo = addBrand2();
//        ProductPojo pojo = new ProductPojo();
//        pojo.setBarcode("barcode1234");
//        pojo.setBrandCategory(brandPojo.getId());
//        pojo.setName("product2");
//        pojo.setMrp(12.45);
//        return productDao.insert(pojo);
//    }
//
//    private InventoryPojo addInventory1() throws ApiException {
//        ProductPojo pojo = addProduct1();
//        InventoryPojo inventoryPojo = new InventoryPojo();
//        inventoryPojo.setProductId(pojo.getId());
//        inventoryPojo.setQuantity(21);
//        return inventoryDao.add(inventoryPojo);
//    }
//
//    private InventoryPojo addInventory2() throws ApiException {
//        ProductPojo pojo = addProduct2();
//        InventoryPojo inventoryPojo = new InventoryPojo();
//        inventoryPojo.setProductId(pojo.getId());
//        inventoryPojo.setQuantity(23);
//        return inventoryDao.add(inventoryPojo);
//    }
//
//
//
//
//
//}
