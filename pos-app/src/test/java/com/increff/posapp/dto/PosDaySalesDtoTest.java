package com.increff.posapp.dto;

import com.increff.posapp.dao.*;
import com.increff.posapp.model.SalesReportData;
import com.increff.posapp.model.SalesReportForm;
import com.increff.posapp.pojo.*;
import com.increff.posapp.service.AbstractUnitTest;
import com.increff.posapp.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PosDaySalesDtoTest extends AbstractUnitTest {

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
    private PosDaySalesDto posDaySalesDto;

    private BrandPojo addBrand(Integer b, Integer c){
        BrandPojo p = new BrandPojo();
        p.setBrand("brand"+b.toString());
        p.setCategory("category"+c.toString());
        return (BrandPojo) brandDao.insert(p);
    }

    private ProductPojo addProduct(Integer brandCategory, Integer p){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("barcode"+p.toString());
        productPojo.setName("product"+p.toString());
        productPojo.setBrandCategory(brandCategory);
        productPojo.setMrp(100.00 + p.doubleValue());
        return (ProductPojo) productDao.insert(productPojo);
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
                    inventoryDao.insert(p);
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
            OrderPojo orderPojo1 = (OrderPojo) orderDao.insert(orderPojo);
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
    public void testUpdateSchedulerNoOrders() throws ApiException {
        posDaySalesDto.updateScheduler();
    }

    @Test
    public void testUpdateScheduler() throws ApiException {
        createOrders(1,2);
        posDaySalesDto.updateScheduler();
    }

    @Test
    public void testGetAll(){
        createOrders(1,2);
        posDaySalesDto.getAll();
    }
}
