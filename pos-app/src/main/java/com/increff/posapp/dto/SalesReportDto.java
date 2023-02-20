package com.increff.posapp.dto;

import com.increff.posapp.model.SalesReportData;
import com.increff.posapp.model.SalesReportForm;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.OrderItemPojo;
import com.increff.posapp.pojo.OrderPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.*;
import com.increff.posapp.util.Converter;
import com.increff.posapp.util.DateTimeUtil;
import com.increff.posapp.util.DoubleUtil;
import com.increff.posapp.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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

    public <T> T getData(SalesReportForm salesReportForm) throws ApiException {
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
                if(!StringUtil.isEmpty(salesReportForm.getBrand() )&& !StringUtil.isEmpty(salesReportForm.getCategory())){
                    if(brandPojo.getBrand().equals(salesReportForm.getBrand()) && brandPojo.getCategory().equals(salesReportForm.getCategory())){
                        setSortedMap1(sm1, orderItemPojo, brandPojo.getBrand(), brandPojo.getCategory());
                        setSortedMap2(sm2, orderItemPojo, brandPojo.getBrand(), brandPojo.getCategory());
                        totalRevenue += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
                    }
                }
                else if(!StringUtil.isEmpty(salesReportForm.getBrand())){
                    if(brandPojo.getBrand().equals(salesReportForm.getBrand())){
                        setSortedMap1(sm1, orderItemPojo, brandPojo.getBrand(), brandPojo.getCategory());
                        setSortedMap2(sm2, orderItemPojo, brandPojo.getBrand(), brandPojo.getCategory());
                        totalRevenue += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
                    }
                    else {
                        continue;
                    }
                }
                else if(!StringUtil.isEmpty(salesReportForm.getCategory())){
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
            Converter.convertToSalesReportData(salesReportData, entry.getKey(), entry.getValue(), quantity);
        }

        return (T) salesReportData;
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
        if(!StringUtil.isEmpty(salesReportForm.getBrand())){
            brandService.getByBrand(salesReportForm.getBrand());
        }
        if(!StringUtil.isEmpty(salesReportForm.getCategory())){
            brandService.getByCategory(salesReportForm.getCategory());
        }
        if(salesReportForm.getStartDate().toLocalDate().isBefore(LocalDate.now().minusYears(2L))){
            throw new ApiException("Enter a date within 2 years");
        }
        if(salesReportForm.getEndDate().toLocalDate().isAfter(LocalDate.now())){
            throw new ApiException("End date should be today's date or a date before today");
        }
        if(ChronoUnit.DAYS.between(salesReportForm.getStartDate().toLocalDate(), salesReportForm.getEndDate().toLocalDate()) > 366){
            throw new ApiException("Difference between the two entered dates must be within one year");
        }
    }
}
