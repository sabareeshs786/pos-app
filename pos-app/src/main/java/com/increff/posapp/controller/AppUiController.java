package com.increff.posapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/ui/brand")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/ui/admin")
	public ModelAndView admin() {
		return mav("user.html");
	}
	
	@RequestMapping(value = "/ui/product")
	public ModelAndView product() {
		return mav("product.html");
	}
	
	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}
	
	@RequestMapping(value = "/ui/orders")
	public ModelAndView order() {
		return mav("orders.html");
	}
	
	@RequestMapping(value = "/ui/order-items/{orderId}/{mode}")
	public ModelAndView orderItem(@PathVariable Integer orderId, @PathVariable String mode)
	{	
		return mav("order-items.html", orderId, mode);
	}

	@RequestMapping(value = "/ui/order-placing")
	public ModelAndView orderPlacing() {
		return mav("order-placing.html");
	}

	@RequestMapping(value = "/ui/brand-report")
	public ModelAndView brandReport(){ return mav("brand-report.html"); }

	@RequestMapping(value = "/ui/inventory-report")
	public ModelAndView inventoryReport(){ return mav("inventory-report.html"); }

	@RequestMapping(value = "/ui/sales-report")
	public ModelAndView salesReport(){ return mav("sales-report.html"); }

	@RequestMapping(value = "/ui/daily-sales-report")
	public ModelAndView scheduler(){ return mav("daily-sales-report.html"); }
}
