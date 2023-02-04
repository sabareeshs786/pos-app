package com.increff.posapp.dto;

import com.increff.posapp.model.BrandData;
import com.increff.posapp.model.InventoryData;
import com.increff.posapp.model.InventoryReportData;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.BrandService;
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
public class BrandReportDto extends InventoryDto{

    @Autowired
    private BrandService brandService;
    private static final Logger logger = Logger.getLogger(InventoryDto.class);
    public Page<BrandData> getBrandReport(String brand, String category, Integer page, Integer size) throws ApiException {
        if(brand.isEmpty() && category.isEmpty()){
            Page<BrandPojo> brandPojoPage = brandService.getAllByPage(page, size);
            return getPage(brandPojoPage, page, size);
        }
        else if(!brand.isEmpty() && !category.isEmpty()){
            BrandPojo brandPojo = brandService.getByBrandAndCategory(brand, category);
            Page<BrandPojo> brandPojoPage = Converter.convertToBrandPojoPage(brandPojo);
            return getPage(brandPojoPage, 0, 1);
        }
        else if(!brand.isEmpty()){
            logger.info("Brand is not empty");
            Page<BrandPojo> brandPojoPage = brandService.getByBrand(brand, page, size);
            return getPage(brandPojoPage, page, size);
        }
        else {
            Page<BrandPojo> brandPojoPage = brandService.getByCategory(category, page, size);
            return getPage(brandPojoPage, page, size);
        }
    }

    private Page<BrandData> getPage(Page<BrandPojo> brandPojoPage, Integer page, Integer size){
        List<BrandData> listBrandData = new ArrayList<>();
        List<BrandPojo> brandPojoList = brandPojoPage.getContent();
        for(BrandPojo p: brandPojoList) {
            listBrandData.add(Converter.convertToBrandData(p));
        }
        return new PageImpl<>(listBrandData, PageRequest.of(page, size), brandPojoPage.getTotalElements());
    }
}
