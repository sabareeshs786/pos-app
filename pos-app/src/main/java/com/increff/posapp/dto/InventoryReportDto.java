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
    private static final Logger logger = Logger.getLogger(InventoryReportDto.class);
    public Page<InventoryReportData> getInventoryReport(String brand, String category, Integer page, Integer size) throws ApiException {

        List<InventoryReportData> inventoryReportDataList = new ArrayList<>();

        if(brand.isEmpty() && category.isEmpty()){
            Page<InventoryPojo> inventoryPojoPage = inventoryService.getAllByPage(page, size);
            List<InventoryPojo> inventoryPojoList = inventoryPojoPage.getContent();

            for(InventoryPojo inventoryPojo: inventoryPojoList){
                ProductPojo productPojo = productService.getById(inventoryPojo.getProductId());
                BrandPojo brandPojo = brandService.getById(productPojo.getBrandCategory());
                inventoryReportDataList.add(Converter.convertToInventoryReportData(inventoryPojo, productPojo, brandPojo));
            }

            return new PageImpl<>(inventoryReportDataList, PageRequest.of(page, size), inventoryPojoPage.getTotalElements());
        }

        else if(!brand.isEmpty() && !category.isEmpty()){
//            BrandPojo brandPojo = brandService.getByBrandAndCategory(brand, category);
//
//            return
        }
        else if(!brand.isEmpty()){
//            Page<BrandPojo> brandPojoPage = brandService.getByBrand(brand, page, size);
//            return
        }
        else {
//            Page<BrandPojo> brandPojoPage = brandService.getByCategory(category, page, size);
//            return
        }
        return null;
    }

//    private Page<InventoryReportData> getPage(Page<BrandPojo> brandPojoPage, Integer page, Integer size){
//        List<BrandData> listBrandData = new ArrayList<>();
//        List<BrandPojo> brandPojoList = brandPojoPage.getContent();
//        for(BrandPojo p: brandPojoList) {
//            listBrandData.add(Converter.convertToBrandData(p));
//        }
//        return new PageImpl<>(listBrandData, PageRequest.of(page, size), brandPojoPage.getTotalElements());
//    }
}
