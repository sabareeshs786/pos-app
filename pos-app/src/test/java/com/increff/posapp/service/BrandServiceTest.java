package com.increff.posapp.service;

import com.increff.posapp.model.BrandData;
import com.increff.posapp.pojo.BrandPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class BrandServiceTest extends AbstractUnitTest {

	@Autowired
	private BrandService brandService;

	@Test(expected = ApiException.class)
	public void testValidateBrandNull() throws ApiException{
		BrandPojo p = new BrandPojo();
		p.setBrand(null);
		p.setCategory("c1");
		brandService.add(p);
	}
	@Test(expected = ApiException.class)
	public void testValidateCategoryNull() throws ApiException{
		BrandPojo p = new BrandPojo();
		p.setBrand("b1");
		p.setCategory(null);
		brandService.add(p);
	}

	@Test(expected = ApiException.class)
	public void testValidateBothNull() throws ApiException{
		BrandPojo p = new BrandPojo();
		p.setBrand(null);
		p.setCategory(null);
		brandService.add(p);
	}

	@Test(expected = ApiException.class)
	public void testValidateBrandEmpty() throws ApiException{
		BrandPojo p = new BrandPojo();
		p.setBrand("");
		p.setCategory("c1");
		brandService.add(p);
	}

	@Test(expected = ApiException.class)
	public void testValidateCategoryEmpty() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("b1");
		p.setCategory("");
		brandService.add(p);
	}

	@Test(expected = ApiException.class)
	public void testValidateDuplicateEntry() throws ApiException {
		BrandPojo p1 = new BrandPojo();
		p1.setBrand("brand1");
		p1.setCategory("category1");
		brandService.add(p1);
		BrandPojo p2 = new BrandPojo();
		p2.setBrand("brand1");
		p2.setCategory("category1");
		brandService.add(p2);
	}

	@Test(expected = ApiException.class)
	public void testValidateDuplicateEntryAndNormalize() throws ApiException {
		BrandPojo p1 = new BrandPojo();
		p1.setBrand("Brand1");
		p1.setCategory("Category1");
		brandService.add(p1);
		BrandPojo p2 = new BrandPojo();
		p2.setBrand("brand1");
		p2.setCategory("category1");
		brandService.add(p2);
	}

	@Test
	public void testNormalize() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("\r  \n BrAnD1 \t\r \n");
		p.setCategory("\t \n \r caTeGoRy1       \n");
		brandService.add(p);
		assertEquals("brand1", p.getBrand());
		assertEquals("category1", p.getCategory());
	}


	@Test
	public void testAdd() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("\r\t  BraNd1   \n");
		p.setCategory("\n   CaTeGoRy1  \t");
		BrandData brandData = brandService.add(p);
		assertEquals("brand1", brandData.getBrand());
		assertEquals("category1", brandData.getCategory());
	}

	@Test(expected = ApiException.class)
	public void testAddBrandNull() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand(null);
		p.setCategory("c1");
		BrandData brandData = brandService.add(p);
	}

	@Test(expected = ApiException.class)
	public void testAddCategoryNull() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory(null);
		BrandData brandData = brandService.add(p);
	}

	@Test(expected = ApiException.class)
	public void testAddBothNull() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand(null);
		p.setCategory(null);
		BrandData brandData = brandService.add(p);
	}

	@Test(expected = ApiException.class)
	public void testAddBrandEmpty() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("");
		p.setCategory("c1");
		BrandData brandData = brandService.add(p);
	}

	@Test(expected = ApiException.class)
	public void testAddCategoryEmpty() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("b1");
		p.setCategory("");
		BrandData brandData = brandService.add(p);
	}

	@Test(expected = ApiException.class)
	public void testAddBothEmpty() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("");
		p.setCategory("");
		BrandData brandData = brandService.add(p);
	}

	@Test
	public void testAddBrandIdAssign() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		BrandData brandData = brandService.add(p);
		assertNotNull(brandData.getId());
	}

	@Test
	public void testGetByIdValid() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		BrandData brandData = brandService.add(p);
		BrandPojo pojo = brandService.getById(brandData.getId());
		assertEquals("brand1", pojo.getBrand());
		assertEquals("category1", pojo.getCategory());
	}

	@Test(expected = ApiException.class)
	public void testGetByIdInvalidId() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		BrandData brandData = brandService.add(p);
		BrandPojo pojo = brandService.getById(brandData.getId() + 1000);
	}

	@Test
	public void testGetByBrandValid() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		BrandData brandData = brandService.add(p);
		List<BrandPojo> brandPojoList = new ArrayList<>();
		brandPojoList = brandService.getByBrand("\r  Brand1   ");
		assertEquals("brand1", brandPojoList.get(0).getBrand());
		assertEquals("category1", brandPojoList.get(0).getCategory());
	}

	@Test(expected = ApiException.class)
	public void testGetByBrandInvalid() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		BrandData brandData = brandService.add(p);
		brandService.getByBrand("\r  BrandName1 \n");
	}

	@Test(expected = ApiException.class)
	public void testGetByBrandNull() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		BrandData brandData = brandService.add(p);
		brandService.getByBrand(null);
	}

	@Test
	public void testGetByCategoryValid() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		BrandData brandData = brandService.add(p);
		List<BrandPojo> brandPojoList = new ArrayList<>();
		brandPojoList = brandService.getByCategory("\r CaTegOry1 \n");
		assertEquals("category1", brandPojoList.get(0).getCategory());
	}

	@Test(expected = ApiException.class)
	public void testGetByCategoryInValid() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		BrandData brandData = brandService.add(p);
		brandService.getByCategory("\r CaTegOryName1 \n");
	}

	@Test(expected = ApiException.class)
	public void testGetByCategoryNull() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		BrandData brandData = brandService.add(p);
		brandService.getByCategory(null);
	}

	@Test
	public void testGetByBrandAndCategory() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		BrandPojo brandPojo = brandService.getByBrandAndCategory(" Brand1", " Category1 ");
		assertEquals("brand1", brandPojo.getBrand());
		assertEquals("category1", brandPojo.getCategory());
	}

	@Test(expected = ApiException.class)
	public void testGetByBrandAndCategoryInvalid() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		brandService.getByBrandAndCategory(" BrandName1", " CategoryName1 ");
	}

	@Test(expected = ApiException.class)
	public void testGetByBrandAndCategoryNull() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		brandService.getByBrandAndCategory(null, null);
	}
	@Test
	public void testUpdateByIdValid() throws ApiException {
		BrandPojo pojo1 = new BrandPojo();
		pojo1.setBrand("brand1");
		pojo1.setCategory("category1");
		BrandData bd = brandService.add(pojo1);
		BrandPojo pojo2 = new BrandPojo();
		pojo2.setBrand("brandname1");
		pojo2.setCategory("categoryname1");
		BrandData brandData = brandService.updateById(bd.getId(), pojo2);
		assertEquals(bd.getId(), brandData.getId());
		assertEquals("brandname1", brandData.getBrand());
		assertEquals("categoryname1", brandData.getCategory());
	}

	@Test
	public void testGetByBrand() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		Page<BrandPojo> pojoPage = brandService.getByBrand(p.getBrand(), 0, 5);
		List<BrandPojo> pojoList = pojoPage.getContent();
		assertTrue(pojoList.size() <= 5);
	}

	@Test(expected = ApiException.class)
	public void testGetByBrandInvalidBrand() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		Page<BrandPojo> pojoPage = brandService.getByBrand("brand2", 0, 5);
	}

	@Test(expected = ApiException.class)
	public void testGetByBrandInvalidPage() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		brandService.getByBrand("brand2", null, 4);
	}

	@Test(expected = ApiException.class)
	public void testGetByBrandInvalidSize() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		brandService.getByBrand("brand2", 0, null);
	}

	@Test(expected = ApiException.class)
	public void testGetByBrandInvalidPageAndSize() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		brandService.getByBrand("brand2", null, null);
	}

	@Test
	public void testGetByCategory() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		Page<BrandPojo> pojoPage = brandService.getByCategory(p.getCategory(),0, 5);
		List<BrandPojo> pojoList = pojoPage.getContent();
		assertTrue(pojoList.size() <= 5);
	}

	@Test(expected = ApiException.class)
	public void testGetBycategoryInvalidCategory() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		Page<BrandPojo> pojoPage = brandService.getByCategory("category2", 0, 5);
	}

	@Test(expected = ApiException.class)
	public void testGetBycategoryInvalidPageAndSize() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		brandService.getByCategory("category2", null, null);
	}
	@Test
	public void testGetAll() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		List<BrandPojo> brandPojoList = brandService.getAll();
		assertTrue(brandPojoList.size() > 0);
	}

	@Test
	public void testGetAllByPage() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		Page<BrandPojo> pojoPage = brandService.getAllByPage(0,5);
		List<BrandPojo> pojoList = pojoPage.getContent();
		assertTrue(pojoList.size() <= 5);
	}

	@Test(expected = ApiException.class)
	public void testGetAllByPageInvalid() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("brand1");
		p.setCategory("category1");
		brandService.add(p);
		brandService.getAllByPage(null,null);
	}


}
