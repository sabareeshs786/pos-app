package com.increff.posapp.dto;

import com.increff.posapp.model.BrandData;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.BrandService;
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
public class BrandReportDto {

    @Autowired
    private BrandService brandService;

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
}
