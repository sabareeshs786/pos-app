package com.increff.posapp.dto;

import com.increff.posapp.model.BrandData;
import com.increff.posapp.model.BrandForm;
import com.increff.posapp.pojo.BrandPojo;
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

    private BrandData addBrand() throws ApiException, IllegalAccessException {
        BrandForm form = new BrandForm();
        form.setBrand("brand1");
        form.setCategory("category1");
        return brandDto.add(form);
    }
    @Test
    public void testAdd() throws ApiException, IllegalAccessException {
        BrandData data = addBrand();
        System.out.println("Data="+data);
        assertEquals("brand1", data.getBrand());
        assertEquals("category1", data.getCategory());
        assertNotNull(data.getId());
    }

    @Test(expected = ApiException.class)
    public void testAddBrandNull() throws ApiException, IllegalAccessException {
        BrandForm form = new BrandForm();
        form.setBrand(null);
        brandDto.add(form);
    }

    @Test(expected = ApiException.class)
    public void testAddBrandEmpty() throws ApiException, IllegalAccessException {
        BrandForm form = new BrandForm();
        form.setBrand("");
        brandDto.add(form);
    }
    @Test(expected = ApiException.class)
    public void testAddBrandNotAlNum() throws ApiException, IllegalAccessException {
        BrandForm form = new BrandForm();
        form.setBrand("/*76%$fftad...");
        brandDto.add(form);
    }
    @Test(expected = ApiException.class)
    public void testAddCategoryNull() throws ApiException, IllegalAccessException {
        BrandForm form = new BrandForm();
        form.setBrand("brand1");
        form.setCategory(null);
        brandDto.add(form);
    }
    @Test(expected = ApiException.class)
    public void testAddCategoryEmpty() throws ApiException, IllegalAccessException {
        BrandForm form = new BrandForm();
        form.setBrand("brand1");
        form.setCategory("");
        brandDto.add(form);
    }

    @Test(expected = ApiException.class)
    public void testAddCategoryNotAlNum() throws ApiException, IllegalAccessException {
        BrandForm form = new BrandForm();
        form.setBrand("brand1");
        form.setCategory("/&$#khihaiuf");
        brandDto.add(form);
    }
    @Test
    public void testGetDataIdNull() throws ApiException, IllegalAccessException {
        BrandData data = addBrand();
        List<BrandData> dataList = brandDto.getData(null, 0, 5).getContent();
        assertTrue(dataList.size() > 0 && dataList.size() <= 5);
    }

    @Test
    public void testGetDataIdNotNull() throws ApiException, IllegalAccessException {
        BrandData data = addBrand();
        List<BrandData> dataList = brandDto.getData(data.getId(), null, null).getContent();
        assertEquals(1, dataList.size());
    }

    @Test(expected = ApiException.class)
    public void testGetDataPageNull() throws ApiException, IllegalAccessException {
        BrandData data = addBrand();
        List<BrandData> dataList = brandDto.getData(null, null, 4).getContent();
        assertEquals(1, dataList.size());
    }

    @Test(expected = ApiException.class)
    public void testGetDataSizeNull() throws ApiException, IllegalAccessException {
        BrandData data = addBrand();
        List<BrandData> dataList = brandDto.getData(null, 0, null).getContent();
        assertEquals(1, dataList.size());
    }

    @Test(expected = ApiException.class)
    public void testGetDataInvalid() throws ApiException {
        brandDto.getData(null, null, null);
    }

    @Test
    public void testUpdateById() throws ApiException, IllegalAccessException {
        BrandData data = addBrand();
        BrandForm form = new BrandForm();
        form.setBrand("brand2");
        form.setCategory("cate2");
        BrandData brandData = brandDto.updateById(data.getId(), form);
        assertEquals("brand2", brandData.getBrand());
        assertEquals("cate2", brandData.getCategory());
    }
}
