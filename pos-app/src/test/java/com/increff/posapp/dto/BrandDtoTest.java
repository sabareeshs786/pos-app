package com.increff.posapp.dto;

import com.increff.posapp.model.BrandData;
import com.increff.posapp.model.BrandForm;
import com.increff.posapp.service.AbstractUnitTest;
import com.increff.posapp.service.ApiException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;
public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;

    private static Logger logger = Logger.getLogger(BrandDtoTest.class);

    private BrandData addBrand() throws ApiException {
        BrandForm form = new BrandForm();
        form.setBrand("brand1");
        form.setCategory("category1");
        System.out.println("BrandDto="+brandDto);
        return brandDto.add(form);
    }
    @Test
    public void testAdd() throws ApiException {
        BrandData data = addBrand();
        System.out.println("Data="+data);
        assertEquals("brand1", data.getBrand());
        assertEquals("category1", data.getCategory());
        assertNotNull(data.getId());
    }

    @Test
    public void testGetDataIdNull() throws ApiException {
        BrandData data = addBrand();
        List<BrandData> dataList = brandDto.getData(null, 0, 5).getContent();
        assertTrue(dataList.size() > 0 && dataList.size() <= 5);
    }

    @Test
    public void testGetDataIdNotNull() throws ApiException {
        BrandData data = addBrand();
        List<BrandData> dataList = brandDto.getData(data.getId(), null, null).getContent();
        assertEquals(1, dataList.size());
    }

    @Test(expected = ApiException.class)
    public void testGetDataInvalid() throws ApiException {
        brandDto.getData(null, null, null);
    }

    @Test
    public void testUpdateById() throws ApiException {
        BrandData data = addBrand();
        BrandForm form = new BrandForm();
        form.setBrand("brand2");
        form.setCategory("cate2");
        BrandData brandData = brandDto.updateById(data.getId(), form);
        assertEquals("brand2", brandData.getBrand());
        assertEquals("cate2", brandData.getCategory());
    }
}
