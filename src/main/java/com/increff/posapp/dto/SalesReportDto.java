package com.increff.posapp.dto;

import com.increff.posapp.model.InventoryData;
import com.increff.posapp.model.InventoryReportData;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.*;
import com.increff.posapp.util.ConverterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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


    public List<InventoryReportData> getall() throws ApiException {
        List<InventoryData> inventoryDataList = super.getAll();
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
        for(InventoryData inventoryData: inventoryDataList){
            System.out.println("Barcode"+inventoryData.getBarcode());
        }
        for(InventoryData inventoryData: inventoryDataList){
            ProductPojo productPojo = productService().getByBarcode(inventoryData.getBarcode());
            System.out.println("Barcode --> "+productPojo.getBarcode());
            System.out.println("Brand_Category: "+productPojo.getBrandCategory());
            BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
            inventoryReportDataList.add(ConverterDto.convertToInventoryReportData(inventoryData, brandPojo));
        }
        return inventoryReportDataList;
    }

    public List<InventoryReportData> getByBrand(String brand) throws ApiException {
        List<InventoryData> inventoryDataList = super.getAll();
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
        for(InventoryData inventoryData: inventoryDataList){
            ProductPojo productPojo = productService().getByBarcode(inventoryData.getBarcode());
            BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
            if(brandPojo.getBrand().equals(brand)) {
                inventoryReportDataList.add(ConverterDto.convertToInventoryReportData(inventoryData, brandPojo));
            }
        }
        return inventoryReportDataList;
    }

    public List<InventoryReportData> getByCategory(String category) throws ApiException {
        List<InventoryData> inventoryDataList = super.getAll();
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
        for(InventoryData inventoryData: inventoryDataList){
            ProductPojo productPojo = productService().getByBarcode(inventoryData.getBarcode());
            BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
            if(brandPojo.getCategory().equals(category)) {
                inventoryReportDataList.add(ConverterDto.convertToInventoryReportData(inventoryData, brandPojo));
            }
        }
        return inventoryReportDataList;
    }

    public List<InventoryReportData> getByBrandAndCategory(String brand, String category) throws ApiException {
        List<InventoryData> inventoryDataList = super.getAll();
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
        for(InventoryData inventoryData: inventoryDataList){
            ProductPojo productPojo = productService().getByBarcode(inventoryData.getBarcode());
            BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
            if(brandPojo.getBrand().equals(brand) && brandPojo.getCategory().equals(category)) {
                inventoryReportDataList.add(ConverterDto.convertToInventoryReportData(inventoryData, brandPojo));
            }
        }
        return inventoryReportDataList;
    }
}
