package com.increff.posapp.dto;

import com.increff.posapp.model.SalesReportData;
import com.increff.posapp.model.SalesReportForm;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.*;
import com.increff.posapp.util.ConverterDto;
import com.increff.posapp.util.DateTimeUtil;
import com.increff.posapp.util.DoubleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Component
public class SalesReportDto extends InventoryDto {

    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;


    public SalesReportData getall() throws ApiException {
        SortedMap<String, Double> sm1 = new TreeMap<>();
        SortedMap<String, Integer> sm2 = new TreeMap<>();
        String brandCategory = null;
        Set<ZonedDateTime> zonedDateTimeSet = new TreeSet<>();
        SalesReportData salesReportData = new SalesReportData();
        List<OrderPojo> orderPojoList = orderService.getAll();
        Double totalRevenue = 0.0;
        for (OrderPojo orderPojo : orderPojoList) {
            List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderPojo.getId());
            for (OrderItemPojo orderItemPojo : orderItemPojoList) {
                ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
                BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                brandCategory = brandPojo.getBrand() + "--" + brandPojo.getCategory();
                if (sm1.containsKey(brandCategory)) {
                    sm1.put(brandCategory, sm1.get(brandCategory) + orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice());
                    sm2.put(brandCategory, sm2.get(brandCategory) + orderItemPojo.getQuantity());
                } else {
                    sm1.put(brandCategory, orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice());
                    sm2.put(brandCategory, orderItemPojo.getQuantity());
                }
                totalRevenue += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
            }
            zonedDateTimeSet.add(orderPojo.getTime());
        }
        List<ZonedDateTime> zonedDateTimeList = new ArrayList<>(zonedDateTimeSet);

        salesReportData.setTotalRevenue(DoubleUtil.roundToString(totalRevenue));

        for (Map.Entry<String, Double> entry : sm1.entrySet()) {
            Integer quantity = sm2.get(entry.getKey());
            ConverterDto.convertToSalesReportData(salesReportData, entry.getKey(), entry.getValue(), quantity);
        }
        return salesReportData;
    }

//    public SalesReportData getData1(SalesReportForm salesReportForm) throws ApiException {
//        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
//        ZonedDateTime zonedDateTimeStart = ZonedDateTime.of(salesReportForm.getStartDate(), zoneId);
//        ZonedDateTime zonedDateTimeEnd = ZonedDateTime.of(salesReportForm.getEndDate(), zoneId);
//        List<OrderPojo> orderPojoList = orderService.getByInterval(zonedDateTimeStart, zonedDateTimeEnd);
//        validate(salesReportForm);
//        return null;
//    }
    public SalesReportData getData(SalesReportForm salesReportForm) throws ApiException {
        ZoneId zoneId = ZoneId.of("Asia/Kolkata");
        ZonedDateTime zonedDateTimeStart = ZonedDateTime.of(salesReportForm.getStartDate(), zoneId);
        ZonedDateTime zonedDateTimeEnd = ZonedDateTime.of(salesReportForm.getEndDate(), zoneId);

        validate(salesReportForm);

        SortedMap<String, Double> sm1 = new TreeMap<>();
        SortedMap<String, Integer> sm2 = new TreeMap<>();
        SalesReportData salesReportData = new SalesReportData();
        Double totalRevenue = 0.0;

        List<OrderPojo> orderPojoList = orderService.getByInterval(zonedDateTimeStart, zonedDateTimeEnd);

        for(OrderPojo orderPojo: orderPojoList){
            List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderPojo.getId());
            for(OrderItemPojo orderItemPojo: orderItemPojoList){
                ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
                BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                if(!salesReportForm.getBrand().isEmpty() && !salesReportForm.getCategory().isEmpty()){
                    if(brandPojo.getBrand().equals(salesReportForm.getBrand()) && brandPojo.getCategory().equals(salesReportForm.getCategory())){
                        setSortedMap1(sm1, orderItemPojo, brandPojo.getBrand(), brandPojo.getCategory());
                        setSortedMap2(sm2, orderItemPojo, brandPojo.getBrand(), brandPojo.getCategory());
                        totalRevenue += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
                    }
                }
                else if(!salesReportForm.getBrand().isEmpty() && salesReportForm.getCategory().isEmpty()){
                    if(brandPojo.getBrand().equals(salesReportForm.getBrand())){
                        setSortedMap1(sm1, orderItemPojo, brandPojo.getBrand(), brandPojo.getCategory());
                        setSortedMap2(sm2, orderItemPojo, brandPojo.getBrand(), brandPojo.getCategory());
                        totalRevenue += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
                    }
                }
                else if(salesReportForm.getBrand().isEmpty() && !salesReportForm.getCategory().isEmpty()){
                    if(brandPojo.getCategory().equals(salesReportForm.getCategory())){
                        setSortedMap1(sm1, orderItemPojo, brandPojo.getBrand(), brandPojo.getCategory());
                        setSortedMap2(sm2, orderItemPojo, brandPojo.getBrand(), brandPojo.getCategory());
                        totalRevenue += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
                    }
                }
                else{
                    setSortedMap1(sm1, orderItemPojo, brandPojo.getBrand(), brandPojo.getCategory());
                    setSortedMap2(sm2, orderItemPojo, brandPojo.getBrand(), brandPojo.getCategory());
                    totalRevenue += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
                }
            }
        }
        salesReportData.setStartDate(DateTimeUtil.getDateTimeString(salesReportForm.getStartDate(),"dd/MM/yyyy"));
        salesReportData.setEndDate(DateTimeUtil.getDateTimeString(salesReportForm.getEndDate(), "dd/MM/yyyy"));
        salesReportData.setTotalRevenue(DoubleUtil.roundToString(totalRevenue));
        for(Map.Entry<String, Double> entry: sm1.entrySet()){
            Integer quantity = sm2.get(entry.getKey());
            ConverterDto.convertToSalesReportData(salesReportData, entry.getKey(), entry.getValue(), quantity);
        }
        System.out.println(salesReportData);
        return salesReportData;
    }

    private void setSortedMap1(SortedMap<String, Double> sm, OrderItemPojo orderItemPojo, String brand, String category){
        String brandCategory = brand + "--" + category;
        if(sm.containsKey(brandCategory)){
            sm.put(brandCategory, sm.get(brandCategory) + orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice());
        }
        else{
            sm.put(brandCategory, orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice());
        }
    }
    private void setSortedMap2(SortedMap<String, Integer> sm, OrderItemPojo orderItemPojo, String brand, String category){
        String brandCategory = brand + "--" + category;
        if(sm.containsKey(brandCategory)){
            sm.put(brandCategory, sm.get(brandCategory) + orderItemPojo.getQuantity());
        }
        else{
            sm.put(brandCategory, orderItemPojo.getQuantity());
        }
    }

    private void validate(SalesReportForm salesReportForm) throws ApiException {
        if(!salesReportForm.getBrand().isEmpty()){
            brandService.getByBrand(salesReportForm.getBrand());
        }
        if(!salesReportForm.getCategory().isEmpty()){
            brandService.getByCategory(salesReportForm.getCategory());
        }
    }
}
