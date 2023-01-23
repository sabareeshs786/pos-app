package com.increff.posapp.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.posapp.pojo.BrandPojo;


public class BrandServiceTest extends AbstractUnitTest {

	@Autowired
	private BrandService service;

	@Test
	public void testAdd() throws ApiException {
		BrandPojo p = new BrandPojo();
		p.setBrand("nestle");
		p.setCategory("dairy");
		service.add(p);
	}

	@Test
	public void testNormalize() {
		BrandPojo p = new BrandPojo();
		p.setBrand("Nestle");
		BrandService.normalize(p);
		assertEquals("nestle", p.getBrand());
	}

}
