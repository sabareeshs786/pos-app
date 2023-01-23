package com.increff.posapp.controller;

import com.increff.posapp.dto.InventoryReportDto;
import com.increff.posapp.model.InventoryReportData;
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
public class SalesReportController {

    @Autowired
    private InventoryReportDto inventoryReportDto;

    @ApiOperation(value = "Gets all brands")
    @RequestMapping(path = "/api/salesreport", method = RequestMethod.GET)
    public List<InventoryReportData> getAll() throws ApiException {
        return inventoryReportDto.getall();
    }

    @ApiOperation(value = "Gets all details of a specific brand")
    @RequestMapping(path = "/api/salesreport/brand/{brand}", method = RequestMethod.GET)
    public List<InventoryReportData> getByBrand(@PathVariable String brand) throws ApiException {
        return inventoryReportDto.getByBrand(brand);
    }

    @ApiOperation(value = "Gets all details of a specific category")
    @RequestMapping(path = "/api/salesreport/category/{category}", method = RequestMethod.GET)
    public List<InventoryReportData> getByCategory(@PathVariable String category) throws ApiException {
        return inventoryReportDto.getByCategory(category);
    }

    @ApiOperation(value = "Gets all details of a specific category")
    @RequestMapping(path = "/api/salesreport/brand/{brand}/category/{category}", method = RequestMethod.GET)
    public List<InventoryReportData> getByBrandAndCategory(@PathVariable String brand, @PathVariable String category) throws ApiException {
        return inventoryReportDto.getByBrandAndCategory(brand, category);
    }
}
