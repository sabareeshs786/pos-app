package com.increff.posapp.controller;

import com.increff.posapp.dto.BrandReportDto;
import com.increff.posapp.dto.InventoryReportDto;
import com.increff.posapp.dto.SalesReportDto;
import com.increff.posapp.model.SalesReportForm;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
public class ReportsController {

    @Autowired
    private BrandReportDto brandReportDto;
    @Autowired
    private InventoryReportDto inventoryReportDto;
    @Autowired
    private SalesReportDto salesReportDto;

    private static final Logger logger = Logger.getLogger(ReportsController.class);
    @ApiOperation(value = "Used to get the brand report")
    @RequestMapping(path = "/api/reports/brand-report", method = RequestMethod.GET)
    public <T> T getBrandReport(
           @RequestParam(required = false) String brand,
           @RequestParam(required = false) String category,
            @RequestParam(name = "page-number", required = false) Integer page,
            @RequestParam(name = "page-size", required = false) Integer size
    ) throws ApiException {
        return brandReportDto.getBrandReport(brand, category, page, size);
    }

    @ApiOperation(value = "Used to get the brand report")
    @RequestMapping(path = "/api/reports/inventory-report", method = RequestMethod.GET)
    public <T> T getInventoryReport(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category,
            @RequestParam(name = "page-number") Integer page,
            @RequestParam(name = "page-size") Integer size
    ) throws ApiException {
        logger.info("Inventory Report >> "+"Brand="+brand+" Category="+category);
        return inventoryReportDto.getInventoryReport(brand, category, page, size);
    }

    @ApiOperation(value = "Used to get the brand report")
    @RequestMapping(path = "/api/reports/sales-report", method = RequestMethod.POST)
    public <T> T getSalesReport(
            @RequestBody SalesReportForm salesReportForm,
            @RequestParam(name = "page-number") Integer page,
            @RequestParam(name = "page-size") Integer size
            ) throws ApiException {
            return salesReportDto.getData(salesReportForm);
    }
}
