package com.increff.posapp.dto;

import com.increff.posapp.model.BrandData;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class ReportsDto {
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    public <T> T getBrandReport(String brand, String category, Integer page, Integer size) throws ApiException {
        brand = StringUtil.toLowerCase(brand);
        category = StringUtil.toLowerCase(category);
        if(page == null && size == null){
            List<BrandPojo> pojos = new ArrayList<>();
            if(StringUtil.isEmpty(brand) && StringUtil.isEmpty(category)){
                pojos = brandService.getAll();
            }
            else if(!StringUtil.isEmpty(brand) && !StringUtil.isEmpty(category)){
                pojos.add(brandService.getByBrandAndCategory(brand, category));
            }
            else if(!StringUtil.isEmpty(category)){
                pojos = brandService.getByCategory(category);
            }
            else {
                pojos = brandService.getByBrand(brand);
            }
            List<BrandData> list = Converter.convertToBrandDataList(pojos);
            return (T) list;
        }
        else if(page == null){
            throw new ApiException("Page can't be empty");
        }
        else if(size == null){
            throw new ApiException("Size can't be empty");
        }
        else if(StringUtil.isEmpty(brand) && StringUtil.isEmpty(category)){
            List<BrandPojo> brandList = brandService.getAll(page, size);
            return (T) getPage(brandList, page, size, brandService.getTotalElements());
        }
        else if(!StringUtil.isEmpty(brand) && !StringUtil.isEmpty(category)){
            BrandPojo brandPojo = brandService.getByBrandAndCategory(brand, category);
            List<BrandData> list = new ArrayList<>();
            list.add(Converter.convertToBrandData(brandPojo));
            return (T) new PageImpl<>(list, PageRequest.of(page, size), 1 );
        }
        else if(!StringUtil.isEmpty(brand)){
            List<BrandPojo> brandPojoList = brandService.getByBrand(brand, page, size);
            return (T) getPage(brandPojoList, page, size, brandService.getByBrandTotalElements(brand));
        }
        else {
            List<BrandPojo> brandList = brandService.getByCategory(category, page, size);
            return (T) getPage(brandList, page, size,
                    brandService.getCategoryTotalElements(category));
        }
    }

    private Page<BrandData> getPage(List<BrandPojo> brandList, Integer page, Integer size, Long totalElements){
        List<BrandData> listBrandData = new ArrayList<>();
        for(BrandPojo p: brandList) {
            listBrandData.add(Converter.convertToBrandData(p));
        }
        return new PageImpl<>(listBrandData,
                PageRequest.of(page, size),
                totalElements);
    }

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
        else if(page == null){
            throw new ApiException("Page can't be empty");
        }
        else if(size == null){
            throw new ApiException("Size can't be empty");
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
