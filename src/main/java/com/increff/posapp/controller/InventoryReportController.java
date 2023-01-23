package com.increff.posapp.controller;

import com.increff.posapp.dto.BrandDto;
import com.increff.posapp.model.BrandData;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class InventoryReportController {

    @Autowired
    private BrandDto brandDto;

    @ApiOperation(value = "Gets all brands")
    @RequestMapping(path = "/api/inventoryreport", method = RequestMethod.GET)
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
}
