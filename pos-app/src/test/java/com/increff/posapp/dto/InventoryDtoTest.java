package com.increff.posapp.dto;

import com.increff.posapp.dao.BrandDao;
import com.increff.posapp.dao.ProductDao;
import com.increff.posapp.model.InventoryData;
import com.increff.posapp.model.InventoryForm;
import com.increff.posapp.model.ProductData;
import com.increff.posapp.model.ProductForm;
import com.increff.posapp.pojo.BrandPojo;
import com.increff.posapp.pojo.ProductPojo;
import com.increff.posapp.service.AbstractUnitTest;
import com.increff.posapp.service.ApiException;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InventoryDtoTest extends AbstractUnitTest {

    @Autowired
    private InventoryDto inventoryDto;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private ProductDao productDao;
    private BrandPojo addBrand(){
        BrandPojo pojo = new BrandPojo();
        pojo.setBrand("brand1");
        pojo.setCategory("category1");
        return brandDao.insert(pojo);
    }
    private ProductPojo addProduct() throws ApiException {
        BrandPojo brandPojo = addBrand();
        ProductPojo pojo = new ProductPojo();
        pojo.setBarcode("barcode123");
        pojo.setBrandCategory(brandPojo.getId());
        pojo.setName("product1");
        pojo.setMrp(123.45);
        return productDao.insert(pojo);
    }

    private InventoryData addInventory() throws ApiException {
        ProductPojo pojo = addProduct();
        InventoryForm form = new InventoryForm();
        form.setBarcode(pojo.getBarcode());
        form.setQuantity(12);
        return inventoryDto.add(form);
    }
    @Test
    public void testAdd() throws ApiException {
        ProductPojo productPojo = addProduct();
        InventoryForm form = new InventoryForm();
        form.setBarcode(productPojo.getBarcode());
        form.setQuantity(12);
        InventoryData data = inventoryDto.add(form);
        assertEquals(productPojo.getBarcode(), data.getBarcode());
        assertEquals(productPojo.getId(), data.getProductId());
        assertEquals(12, data.getQuantity().intValue());
    }

    @Test
    public void testGetDataProductIdNull() throws ApiException {
        ProductPojo productPojo = addProduct();
        InventoryForm form = new InventoryForm();
        form.setBarcode(productPojo.getBarcode());
        form.setQuantity(12);
        InventoryData data = inventoryDto.add(form);
        List<InventoryData> dataList = inventoryDto.getData(null, 0, 5).getContent();
        assertTrue(dataList.size() > 0 && dataList.size() <= 5);
    }

    @Test
    public void testGetDataProductId() throws ApiException {
        ProductPojo productPojo = addProduct();
        InventoryForm form = new InventoryForm();
        form.setBarcode(productPojo.getBarcode());
        form.setQuantity(12);
        InventoryData data = inventoryDto.add(form);
        List<InventoryData> dataList = inventoryDto.getData(productPojo.getId(), null, null).getContent();
        assertEquals(1, dataList.size());
    }

    @Test(expected = ApiException.class)
    public void testGetData() throws ApiException {
        inventoryDto.getData(null, null, null);
    }

    @Test
    public void testUpdate() throws ApiException {
        ProductPojo pojo = addProduct();
        InventoryForm form = new InventoryForm();
        form.setBarcode(pojo.getBarcode());
        form.setQuantity(12);
        InventoryData data = inventoryDto.add(form);
        form.setBarcode(pojo.getBarcode());
        form.setQuantity(21);
        InventoryData dataReturned = inventoryDto.updateByProductId(pojo.getId(), form);
        assertEquals(pojo.getId(), dataReturned.getProductId());
        assertEquals(pojo.getBarcode(), dataReturned.getBarcode());
        assertEquals(21, dataReturned.getQuantity().intValue());
    }
}
