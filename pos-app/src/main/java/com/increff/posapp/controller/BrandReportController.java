package com.increff.posapp.controller;

import com.increff.posapp.dto.BrandDto;
import com.increff.posapp.dto.BrandReportDto;
import com.increff.posapp.model.BrandData;
import com.increff.posapp.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api
@RestController
public class BrandReportController {

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private BrandReportDto brandReportDto;

    @ApiOperation(value = "Gets all brands")
    @RequestMapping(path = "/api/brandreport", method = RequestMethod.GET)
    public List<BrandData> getAll() throws ApiException {
        return brandDto.getAll();
    }

    @ApiOperation(value = "Gets all details of a specific brand")
    @RequestMapping(path = "/api/brandreport/brand/{brand}", method = RequestMethod.GET)
    public List<BrandData> getByBrand(@PathVariable String brand) throws ApiException {
        return brandDto.getByBrand(brand);
    }

    @ApiOperation(value = "Gets all details of a specific category")
    @RequestMapping(path = "/api/brandreport/category/{category}", method = RequestMethod.GET)
    public List<BrandData> getByCategory(@PathVariable String category) throws ApiException {
        return brandDto.getByCategory(category);
    }

    @ApiOperation(value = "Gets all details of a specific category")
    @RequestMapping(path = "/api/brandreport/brand/{brand}/category/{category}", method = RequestMethod.GET)
    public BrandData getByBrandAndCategory(@PathVariable String brand, @PathVariable String category) throws ApiException {
        return brandDto.getByBrandandCategory(brand, category);
    }

//    @RequestMapping(value = "/api/brandreport/download", method = RequestMethod.GET)
//    public ResponseEntity<byte[]> download() throws IOException, ApiException {
//        return brandReportDto.getAll();
//    }

}
