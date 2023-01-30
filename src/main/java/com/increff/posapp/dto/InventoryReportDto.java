package com.increff.posapp.dto;

import com.increff.posapp.model.InventoryData;
import com.increff.posapp.model.InventoryReportData;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.BrandService;
import com.increff.posapp.util.ConverterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryReportDto extends InventoryDto{

    @Autowired
    private BrandService brandService;

    public List<InventoryReportData> getall() throws ApiException {
        Page<InventoryData> inventoryDataList = super.getAll(0, 10);
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
        for(InventoryData inventoryData: inventoryDataList.getContent()){
            ProductPojo productPojo = productService().getByBarcode(inventoryData.getBarcode());
            BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
            inventoryReportDataList.add(ConverterDto.convertToInventoryReportData(inventoryData, brandPojo));
        }
        return inventoryReportDataList;
    }

    public List<InventoryReportData> getByBrand(String brand) throws ApiException {
        Page<InventoryData> inventoryDataList = super.getAll(0, 10);
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
        for(InventoryData inventoryData: inventoryDataList.getContent()){
            ProductPojo productPojo = productService().getByBarcode(inventoryData.getBarcode());
            BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
            if(brandPojo.getBrand().equals(brand)) {
                inventoryReportDataList.add(ConverterDto.convertToInventoryReportData(inventoryData, brandPojo));
            }
        }
        return inventoryReportDataList;
    }

    public List<InventoryReportData> getByCategory(String category) throws ApiException {
        Page<InventoryData> inventoryDataList = super.getAll(0, 10);
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
        for(InventoryData inventoryData: inventoryDataList.getContent()){
            ProductPojo productPojo = productService().getByBarcode(inventoryData.getBarcode());
            BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
            if(brandPojo.getCategory().equals(category)) {
                inventoryReportDataList.add(ConverterDto.convertToInventoryReportData(inventoryData, brandPojo));
            }
        }
        return inventoryReportDataList;
    }

    public List<InventoryReportData> getByBrandAndCategory(String brand, String category) throws ApiException {
        Page<InventoryData> inventoryDataList = super.getAll(0,10);
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
        for(InventoryData inventoryData: inventoryDataList.getContent()){
            ProductPojo productPojo = productService().getByBarcode(inventoryData.getBarcode());
            BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
            if(brandPojo.getBrand().equals(brand) && brandPojo.getCategory().equals(category)) {
                inventoryReportDataList.add(ConverterDto.convertToInventoryReportData(inventoryData, brandPojo));
            }
        }
        return inventoryReportDataList;
    }
}
