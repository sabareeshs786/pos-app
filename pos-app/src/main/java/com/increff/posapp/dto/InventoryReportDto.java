package com.increff.posapp.dto;

import com.increff.posapp.model.InventoryReportData;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.BrandService;
import com.increff.posapp.service.InventoryService;
import com.increff.posapp.service.ProductService;
import com.increff.posapp.util.Converter;
import com.increff.posapp.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryReportDto extends InventoryDto{

    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    public <T> T getInventoryReport(String brand, String category, Integer page, Integer size) throws ApiException {
        brand = StringUtil.toLowerCase(brand);
        category = StringUtil.toLowerCase(category);
        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();
        List<InventoryPojo> inventoryPojoList = inventoryService.getAll();
        if(page == null && size == null){
            if(StringUtil.isEmpty(brand) && StringUtil.isEmpty(category)){
                for(InventoryPojo inventoryPojo: inventoryPojoList){
                    ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
                    BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                    inventoryReportDataList.add(Converter.convertToInventoryReportData(inventoryPojo, productPojo, brandPojo));
                }
                validate(inventoryReportDataList);
            }
            else if(!StringUtil.isEmpty(brand) && !StringUtil.isEmpty(category)){
                for(InventoryPojo inventoryPojo: inventoryPojoList){
                    ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
                    BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                    if(brandPojo.getBrand().equals(brand) && brandPojo.getCategory().equals(category)){
                        inventoryReportDataList.add(Converter.convertToInventoryReportData(inventoryPojo, productPojo, brandPojo));
                    }
                }
                validate(inventoryReportDataList);
            }
            else if(!StringUtil.isEmpty(brand)){
                for(InventoryPojo inventoryPojo: inventoryPojoList){
                    ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
                    BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                    if(brandPojo.getBrand().equals(brand)){
                        inventoryReportDataList.add(Converter.convertToInventoryReportData(inventoryPojo, productPojo, brandPojo));
                    }
                }
                validate(inventoryReportDataList);
            }
            else {
                for(InventoryPojo inventoryPojo: inventoryPojoList){
                    ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
                    BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                    if(brandPojo.getCategory().equals(category)){
                        inventoryReportDataList.add(Converter.convertToInventoryReportData(inventoryPojo, productPojo, brandPojo));
                    }
                }
                validate(inventoryReportDataList);
            }
            return (T) inventoryReportDataList;
        }
        else if(StringUtil.isEmpty(brand) && StringUtil.isEmpty(category)){
            for(InventoryPojo inventoryPojo: inventoryPojoList){
                ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
                BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                inventoryReportDataList.add(Converter.convertToInventoryReportData(inventoryPojo, productPojo, brandPojo));
            }
            validate(inventoryReportDataList);
            return (T) getPage(inventoryReportDataList, page, size);
        }

        else if(!StringUtil.isEmpty(brand) && !StringUtil.isEmpty(category)){
            for(InventoryPojo inventoryPojo: inventoryPojoList){
                ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
                BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                if(brandPojo.getBrand().equals(brand) && brandPojo.getCategory().equals(category)){
                    inventoryReportDataList.add(
                            Converter.convertToInventoryReportData(inventoryPojo, productPojo, brandPojo)
                    );
                }
            }
            validate(inventoryReportDataList);
            return (T) getPage(inventoryReportDataList, page, size);
        }
        else if(!StringUtil.isEmpty(brand)){
            for(InventoryPojo inventoryPojo: inventoryPojoList){
                ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
                BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                if(brandPojo.getBrand().equals(brand)){
                    inventoryReportDataList.add(
                            Converter.convertToInventoryReportData(inventoryPojo, productPojo, brandPojo)
                    );
                }
            }
            validate(inventoryReportDataList);
            return (T) getPage(inventoryReportDataList, page, size);
        }
        else {
            for(InventoryPojo inventoryPojo: inventoryPojoList){
                ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
                BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                if(brandPojo.getCategory().equals(category)){
                    inventoryReportDataList.add(
                            Converter.convertToInventoryReportData(inventoryPojo, productPojo, brandPojo)
                    );
                }
            }
            validate(inventoryReportDataList);
            return (T) getPage(inventoryReportDataList, page, size);
        }
    }
    private <T> T getPage(List<InventoryReportData> inventoryReportDataList, Integer page, Integer size){
        List<InventoryReportData> inventoryReportDataList1 = new ArrayList<>();
        Long totalElements = (long) inventoryReportDataList.size();
        Integer start = page*size;
        Integer end = start + size - 1;
        for(int i=start; i <= end && i < inventoryReportDataList.size(); i++){
            inventoryReportDataList1.add(inventoryReportDataList.get(i));
        }
        return (T) new PageImpl<>(inventoryReportDataList1, PageRequest.of(page, size), inventoryReportDataList.size());
    }

    private void validate(List<InventoryReportData> list) throws ApiException {
        if(list.isEmpty()){
            throw new ApiException("No items available with the given details");
        }
    }
}
