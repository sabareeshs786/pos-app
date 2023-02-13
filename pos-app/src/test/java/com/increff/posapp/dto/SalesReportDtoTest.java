package com.increff.posapp.dto;

import com.increff.posapp.dao.*;
import com.increff.posapp.model.*;
import com.increff.posapp.pojo.*;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SalesReportDtoTest extends AbstractUnitTest {

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
    @Autowired
    private SalesReportDto salesReportDto;

    private BrandPojo addBrand(Integer b, Integer c){
        BrandPojo p = new BrandPojo();
        p.setBrand("brand"+b.toString());
        p.setCategory("category"+c.toString());
        return brandDao.insert(p);
    }

    private ProductPojo addProduct(Integer brandCategory, Integer p){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode"+p.toString());
        productPojo.setName("product"+p.toString());
        productPojo.setBrandCategory(brandCategory);
        productPojo.setMrp(100.00 + p.doubleValue());
        return productDao.insert(productPojo);
    }

    private void createInventory(){
        int k = 1;
        for(int i=1; i<=2; i++){
            for(int j=1; j<=2; j++){
                BrandPojo brandPojo = addBrand(i,j);
                Integer brandCategory = brandPojo.getId();
                int t = 0;
                while(t < 2){
                    ProductPojo pojo = addProduct(brandCategory,k);
                    InventoryPojo p = new InventoryPojo();
                    p.setProductId(pojo.getId());
                    p.setQuantity(12+t);
                    inventoryDao.add(p);
                    t++;
                    k++;
                }
            }
        }
    }

    private List<Integer> createOrders(Integer s, Integer e){
        createInventory();
        List<Integer> list = new ArrayList<>();
        for(int i=1; i<=2; i++) {
            OrderPojo orderPojo = new OrderPojo("Asia/Kolkata");
            OrderPojo orderPojo1 =orderDao.insert(orderPojo);
            list.add(orderPojo1.getId());
            for (int j = s; j <= e; j++) {
                OrderItemPojo pojo = new OrderItemPojo();
                pojo.setOrderId(orderPojo.getId());
                pojo.setProductId(productDao.selectByBarcode("barcode" + j).getId());
                pojo.setQuantity(2);
                pojo.setSellingPrice(98.01);
                orderItemDao.insert(pojo);
            }
        }
        return list;
    }

    @Test
    public void testGetData() throws ApiException {
        createOrders(1,2);
        SalesReportForm form = new SalesReportForm();
        form.setStartDate(LocalDateTime.now().minusDays(10L));
        form.setEndDate(LocalDateTime.now().plusMinutes(10L));
        form.setBrand("");
        form.setCategory("");
        SalesReportData data = salesReportDto.getData(form);

        assertEquals(
                LocalDateTime
                        .now()
                        .minusDays(10L)
                        .toLocalDate()
                        .format(
                                DateTimeFormatter
                                        .ofPattern("dd/MM/yyyy")),
                data.getStartDate()
        );
        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), data.getEndDate());
//        assertEquals("784.08", data.getTotalRevenue());
    }

    @Test
    public void testGetDataBrand() throws ApiException {
        List<Integer> orderIds = createOrders(1,2);
        SalesReportForm form = new SalesReportForm();
        form.setStartDate(LocalDateTime.now().minusDays(10L));
        form.setEndDate(LocalDateTime.now());
        form.setBrand("brand1");
        form.setCategory("");
        SalesReportData data = salesReportDto.getData(form);
        assertEquals(
                LocalDateTime
                        .now()
                        .minusDays(10L)
                        .toLocalDate()
                        .format(
                                DateTimeFormatter
                                        .ofPattern("dd/MM/yyyy")),
                data.getStartDate()
        );
        assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), data.getEndDate());
//        assertEquals("784.08", data.getTotalRevenue());
    }
    @Test
    public void testGetDataCategory() throws ApiException {
        List<Integer> orderIds = createOrders(1,2);
        SalesReportForm form = new SalesReportForm();
        form.setStartDate(LocalDateTime.now().minusDays(10L));
        form.setEndDate(LocalDateTime.now());
        form.setBrand("");
        form.setCategory("category1");
        SalesReportData data = salesReportDto.getData(form);
        assertEquals(
                LocalDateTime
                        .now()
                        .minusDays(10L)
                        .toLocalDate()
                        .format(
                                DateTimeFormatter
                                        .ofPattern("dd/MM/yyyy")),
                data.getStartDate()
        );
//        assertEquals("196.20", data.getTotalRevenue());
    }

    @Test
    public void testGetDataBrandCategory() throws ApiException {
        List<Integer> orderIds = createOrders(1,2);
        SalesReportForm form = new SalesReportForm();
        form.setStartDate(LocalDateTime.now().minusDays(10L));
        form.setEndDate(LocalDateTime.now());
        form.setBrand("brand1");
        form.setCategory("category1");
        SalesReportData data = salesReportDto.getData(form);
        assertEquals(
                LocalDateTime
                        .now()
                        .minusDays(10L)
                        .toLocalDate()
                        .format(
                                DateTimeFormatter
                                        .ofPattern("dd/MM/yyyy")),
                data.getStartDate()
        );
//        assertEquals("98.01", data.getTotalRevenue());
    }

    @Test(expected = ApiException.class)
    public void testValidateDateInvalid() throws ApiException {
        SalesReportForm form = new SalesReportForm();
        form.setStartDate(LocalDateTime.now().minusDays(10L));
        form.setEndDate(LocalDateTime.now().plusDays(20L));
        form.setBrand("");
        form.setCategory("");
        SalesReportData data = salesReportDto.getData(form);
    }

    @Test(expected = ApiException.class)
    public void testValidateDaysInvalid() throws ApiException {
        SalesReportForm form = new SalesReportForm();
        form.setStartDate(LocalDateTime.now().minusYears(2L));
        form.setEndDate(LocalDateTime.now());
        form.setBrand("");
        form.setCategory("");
        SalesReportData data = salesReportDto.getData(form);
    }
}
