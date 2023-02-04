package com.increff.posapp.controller;

import com.increff.posapp.dto.BrandReportDto;
import com.increff.posapp.dto.InventoryReportDto;
import com.increff.posapp.model.BrandData;
import com.increff.posapp.model.InventoryReportData;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
public class ReportsController {

    @Autowired
    private BrandReportDto brandReportDto;
    @Autowired
    private InventoryReportDto inventoryReportDto;

    private static final Logger logger = Logger.getLogger(ReportsController.class);
    @ApiOperation(value = "Used to get the brand report")
    @RequestMapping(path = "/api/reports/brandreport", method = RequestMethod.GET)
    public Page<BrandData> getBrandReport(
            @RequestParam(name = "brand") String brand,
            @RequestParam(name = "category") String category,
            @RequestParam(name = "pagenumber") Integer page,
            @RequestParam Integer size
    ) throws ApiException {
        return brandReportDto.getBrandReport(brand, category, page, size);
    }

    @ApiOperation(value = "Used to get the brand report")
    @RequestMapping(path = "/api/reports/inventoryreport", method = RequestMethod.GET)
    public Page<InventoryReportData> getInventoryReport(
            @RequestParam(name = "brand") String brand,
            @RequestParam(name = "category") String category,
            @RequestParam(name = "pagenumber") Integer page,
            @RequestParam Integer size
    ) throws ApiException {
        logger.info("Inventory Report >> "+"Brand="+brand+" Category="+category);
        return inventoryReportDto.getInventoryReport(brand, category, page, size);
    }
}
