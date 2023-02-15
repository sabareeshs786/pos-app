package com.increff.posapp.dto;

import com.increff.posapp.dao.BrandDao;
import com.increff.posapp.dao.InventoryDao;
import com.increff.posapp.model.ProductData;
import com.increff.posapp.model.ProductForm;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.InventoryPojo;
import com.increff.posapp.service.AbstractUnitTest;
import com.increff.posapp.service.ApiException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ProductDtoTest extends AbstractUnitTest {

    @Autowired
    private ProductDto productDto;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private InventoryDao inventoryDao;
    private static Logger logger = Logger.getLogger(ProductDtoTest.class);

    private BrandPojo addBrand(){
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand1");
        pojo.setCategory("category1");
        return brandDao.insert(pojo);
    }
    private ProductData addProduct() throws ApiException {
        ProductForm form = new ProductForm();
        form.setBarcode("asd3455t5");
        form.setBrand("brand1");
        form.setCategory("category1");
        form.setName("product1");
        form.setMrp(123.45);
        return productDto.add(form);
    }

    @Test
    public void testAdd() throws ApiException {
        BrandPojo pojo = addBrand();
        ProductData data = addProduct();
        assertEquals("brand1", data.getBrand());
        assertEquals("category1", data.getCategory());
        assertEquals("product1", data.getName());
        assertEquals("123.45", data.getMrp().toString());
        assertEquals("asd3455t5", data.getBarcode());
        assertEquals(pojo.getId(), data.getBrandCategory());
    }

    @Test
    public void testGetById() throws ApiException {
        BrandPojo pojo = addBrand();
        ProductData data1 = addProduct();
        ProductData data = productDto.getById(data1.getId()).getContent().get(0);
        assertEquals("brand1", data.getBrand());
        assertEquals("category1", data.getCategory());
        assertEquals("product1", data.getName());
        assertEquals("123.45", data.getMrp().toString());
        assertEquals("asd3455t5", data.getBarcode());
        assertEquals(pojo.getId(), data.getBrandCategory());
    }

    @Test
    public void testGetByBarcode() throws ApiException {
        BrandPojo pojo = addBrand();
        ProductData data1 = addProduct();
        ProductData data = productDto.getByBarcode(data1.getBarcode());
        assertEquals("brand1", data.getBrand());
        assertEquals("category1", data.getCategory());
        assertEquals("product1", data.getName());
        assertEquals("123.45", data.getMrp().toString());
        assertEquals("asd3455t5", data.getBarcode());
        assertEquals(pojo.getId(), data.getBrandCategory());
    }

    @Test
    public void testGetAll() throws ApiException {
        BrandPojo pojo = addBrand();
        ProductData data1 = addProduct();
        List<ProductData> dataList = productDto.getAll(0,5).getContent();
        assertTrue(dataList.size() > 0 && dataList.size() <= 5);
    }

    @Test
    public void testUpdateById() throws ApiException {
        BrandPojo pojo = addBrand();
        ProductData data1 = addProduct();
        ProductForm form = new ProductForm();
        form.setBarcode("asd34455");
        form.setBrand("brand1");
        form.setCategory("category1");
        form.setName("product2");
        form.setMrp(13.45);
        ProductData data = productDto.updateById(data1.getId(), form);
        assertEquals("brand1", data.getBrand());
        assertEquals("category1", data.getCategory());
        assertEquals("product2", data.getName());
        assertEquals("13.45", data.getMrp().toString());
        assertEquals("asd34455", data.getBarcode());
        assertEquals(data1.getBrandCategory(), data.getBrandCategory());
        assertEquals(0, data.getQuantity().intValue());
    }

    @Test
    public void testGetDataIdNull() throws ApiException {
        BrandPojo pojo = addBrand();
        ProductData data1 = addProduct();
        List<ProductData> dataList = productDto.getData(null, 0, 5).getContent();
        assertTrue(dataList.size()>0 && dataList.size() <=5);
    }

    @Test
    public void testGetDataId() throws ApiException {
        BrandPojo pojo = addBrand();
        ProductData data1 = addProduct();
        List<ProductData> dataList = productDto.getData(data1.getId(), null, null).getContent();
        assertTrue(dataList.size()>0 && dataList.size() <=5);
    }

    @Test(expected = ApiException.class)
    public void testGetDataInvalid() throws ApiException {
        productDto.getData(null, null, null);
    }
}
