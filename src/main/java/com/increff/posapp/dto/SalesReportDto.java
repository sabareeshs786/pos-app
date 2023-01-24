package com.increff.posapp.dto;

import com.increff.posapp.model.InventoryData;
import com.increff.posapp.model.InventoryReportData;
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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

@Component
public class SalesReportDto extends InventoryDto{

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
        Double grandTotal = 0.0;
        for(OrderPojo orderPojo: orderPojoList){
            List<OrderItemPojo> orderItemPojoList = orderItemService.getByOrderId(orderPojo.getId());
            for(OrderItemPojo orderItemPojo: orderItemPojoList){
                ProductPojo productPojo = productService.getById(orderItemPojo.getProductId());
                BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                brandCategory = brandPojo.getBrand() +"--" + brandPojo.getCategory();
                if(sm1.containsKey(brandCategory) && sm2.containsKey(brandCategory)){
                    sm1.put(brandCategory, sm1.get(brandCategory) + orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice());
                    sm2.put(brandCategory, sm2.get(brandCategory) + orderItemPojo.getQuantity());
                }
                else{
                    sm1.put(brandCategory, orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice());
                    sm2.put(brandCategory, orderItemPojo.getQuantity());
                }
                grandTotal += orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
            }
            zonedDateTimeSet.add(orderPojo.getTime());
        }
        List<ZonedDateTime> zonedDateTimeList = new ArrayList<>(zonedDateTimeSet);
        salesReportData.setStartDate(DateTimeUtil.getDateTime(zonedDateTimeList.get(0), "dd/MM/yyyy - hh:mm:ss"));
        salesReportData.setEndDate(DateTimeUtil.getDateTime(zonedDateTimeList.get(zonedDateTimeList.size() -1), "dd/MM/yyyy - hh:mm:ss"));
        salesReportData.setGrandTotal(DoubleUtil.roundToString(grandTotal));
        for(Map.Entry<String, Double> entry: sm1.entrySet()){
            Integer quantity = sm2.get(entry.getKey());
            ConverterDto.convertToSalesReportData(salesReportData, entry.getKey(), entry.getValue(), quantity);
        }
        return salesReportData;
    }

    public List<SalesReportData> getData(SalesReportForm salesReportForm) throws ApiException {
        return null;
    }
}
