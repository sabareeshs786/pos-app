package com.increff.posapp.dto;

import com.increff.posapp.model.BrandData;
import com.increff.posapp.model.InventoryData;
import com.increff.posapp.model.InventoryReportData;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.BrandService;
import com.increff.posapp.util.Converter;
import com.increff.posapp.util.StringUtil;
import com.increff.posapp.util.Validator;
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
    public <T> T getBrandReport(String brand, String category, Integer page, Integer size) throws ApiException {
        brand = StringUtil.toLowerCase(brand);
        category = StringUtil.toLowerCase(category);
        if(page == null && size == null){
            List<BrandData> list = new ArrayList<>();
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
            for(BrandPojo p : pojos){
                list.add(Converter.convertToBrandData(p));
            }
            return (T) list;
        }
        else if(StringUtil.isEmpty(brand) && StringUtil.isEmpty(category)){
            Page<BrandPojo> brandPojoPage = brandService.getAllByPage(page, size);
            return (T) getPage(brandPojoPage, page, size);
        }
        else if(!StringUtil.isEmpty(brand) && !StringUtil.isEmpty(category)){
            BrandPojo brandPojo = brandService.getByBrandAndCategory(brand, category);
            Page<BrandPojo> brandPojoPage = Converter.convertToBrandPojoPage(brandPojo);
            return (T) getPage(brandPojoPage, 0, 1);
        }
        else if(!StringUtil.isEmpty(brand)){
            Page<BrandPojo> brandPojoPage = brandService.getByBrand(brand, page, size);
            return (T) getPage(brandPojoPage, page, size);
        }
        else {
            Page<BrandPojo> brandPojoPage = brandService.getByCategory(category, page, size);
            return (T) getPage(brandPojoPage, page, size);
        }
    }

    private Page<BrandData> getPage(Page<BrandPojo> brandPojoPage, Integer page, Integer size){
        List<BrandData> listBrandData = new ArrayList<>();
        List<BrandPojo> brandPojoList = brandPojoPage.getContent();
        for(BrandPojo p: brandPojoList) {
            listBrandData.add(Converter.convertToBrandData(p));
        }
        return new PageImpl<>(listBrandData,
                PageRequest.of(page, size),
                brandPojoPage.getTotalElements());
    }
}
