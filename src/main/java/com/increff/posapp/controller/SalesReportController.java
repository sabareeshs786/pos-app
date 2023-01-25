package com.increff.posapp.controller;

import com.increff.posapp.dto.InventoryReportDto;
import com.increff.posapp.dto.SalesReportDto;
import com.increff.posapp.model.InventoryReportData;
import com.increff.posapp.model.SalesReportData;
import com.increff.posapp.model.SalesReportForm;
import com.increff.posapp.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class SalesReportController {

    @Autowired
    private SalesReportDto salesReportDto;

    @ApiOperation(value = "Gets all brands")
    @RequestMapping(path = "/api/salesreport", method = RequestMethod.GET)
    public SalesReportData getAll() throws ApiException {
        return salesReportDto.getall();
    }

    @ApiOperation(value = "Gets all details of a specific brand")
    @RequestMapping(path = "/api/salesreport/filter", method = RequestMethod.POST)
    public SalesReportData getData(@RequestBody SalesReportForm salesReportForm) throws ApiException {
        return salesReportDto.getData(salesReportForm);
    }
}
