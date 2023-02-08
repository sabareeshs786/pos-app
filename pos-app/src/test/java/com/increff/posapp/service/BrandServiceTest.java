package com.increff.posapp.service;

import com.increff.posapp.pojo.BrandPojo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;


public class BrandServiceTest extends AbstractUnitTest {

	@Autowired
	private BrandService brandService;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testValidate1() throws ApiException{
		BrandPojo p = new BrandPojo();
		p.setBrand(null);
		p.setCategory("c1");
		thrown.expect(ApiException.class);
		thrown.expectMessage("Brand is not obtained in the backend");
		brandService.validate(p);
	}
	@Test
	public void testValidate2() throws ApiException{
		BrandPojo p = new BrandPojo();
		p.setBrand("b1");
		p.setCategory(null);
		thrown.expect(ApiException.class);
		thrown.expectMessage("Category is not obtained in the backend");
		brandService.validate(p);
	}

	@Test
	public void testValidate3() throws ApiException{
		BrandPojo p = new BrandPojo();
		p.setBrand(null);
		p.setCategory(null);
		thrown.expect(ApiException.class);
		thrown.expectMessage("Brand is not obtained in the backend");
		brandService.validate(p);
	}

	@Test
	public void testValidate4() throws ApiException{
		BrandPojo p = new BrandPojo();
		p.setBrand("");
		p.setCategory("c1");
		thrown.expect(ApiException.class);
		thrown.expectMessage("Brand can't be empty");
		brandService.validate(p);
	}

	@Test
	public void testValidate5() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("b1");
		p.setCategory("");
		thrown.expect(ApiException.class);
		thrown.expectMessage("Category can't be empty");
		brandService.validate(p);
	}

	@Test
	public void testValidate6() throws ApiException {
		BrandPojo p1 = new BrandPojo();
		p1.setBrand("brand1");
		p1.setCategory("category1");
		brandService.add(p1);
		BrandPojo p2 = new BrandPojo();
		p2.setBrand("brand1");
		p2.setCategory("category1");
		thrown.expect(ApiException.class);
		thrown.expectMessage("The entered brand and category combination already exists\nEnter a different brand or category");
		brandService.validate(p2);
	}

	@Test
	public void testValidate7() throws ApiException {
		BrandPojo p1 = new BrandPojo();
		p1.setBrand("Brand1");
		p1.setCategory("Category1");
		brandService.add(p1);
		BrandPojo p2 = new BrandPojo();
		p2.setBrand("brand1");
		p2.setCategory("category1");
		thrown.expect(ApiException.class);
		thrown.expectMessage("The entered brand and category combination already exists\nEnter a different brand or category");
		brandService.validate(p2);
	}

	@Test
	public void testNormalize1() {
		BrandPojo p = new BrandPojo();
		p.setBrand("Nestle");
		p.setCategory("Diary");
		BrandService.normalize(p);
		assertEquals("nestle", p.getBrand());
		assertEquals("diary", p.getCategory());
	}
	@Test
	public void testNormalize2() {
		BrandPojo p = new BrandPojo();
		p.setBrand("NESTLE");
		p.setCategory("DIARY");
		BrandService.normalize(p);
		assertEquals("nestle", p.getBrand());
		assertEquals("diary", p.getCategory());
	}

	@Test
	public void testNormalize3() {
		BrandPojo p = new BrandPojo();
		p.setBrand("NeSTLe");
		p.setCategory("DiArY");
		BrandService.normalize(p);
		assertEquals("nestle", p.getBrand());
		assertEquals("diary", p.getCategory());
	}

	@Test
	public void testNormalize4() {
		BrandPojo p = new BrandPojo();
		p.setBrand("Nestle");
		p.setCategory("Diary");
		BrandService.normalize(p);
		assertEquals("nestle", p.getBrand());
		assertEquals("diary", p.getCategory());
	}

	@Test
	public void testNormalize5() {
		BrandPojo p = new BrandPojo();
		p.setBrand("      Nestle    ");
		p.setCategory("    Diary    ");
		BrandService.normalize(p);
		assertEquals("nestle", p.getBrand());
		assertEquals("diary", p.getCategory());
	}

	@Test
	public void testNormalize6() {
		BrandPojo p = new BrandPojo();
		p.setBrand("\n\r     NESTLE     ");
		p.setCategory("     DIARY     \n\r");
		BrandService.normalize(p);
		assertEquals("nestle", p.getBrand());
		assertEquals("diary", p.getCategory());
	}

	@Test
	public void testNormalize7() {
		BrandPojo p = new BrandPojo();
		p.setBrand("     NeStLe\t\n\r");
		p.setCategory("   \r \nDiaRY   \n\r");
		BrandService.normalize(p);
		assertEquals("nestle", p.getBrand());
		assertEquals("diary", p.getCategory());
	}

	@Test
	public void testAdd1() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("nestle");
		p.setCategory("dairy");
		brandService.add(p);
	}


}
