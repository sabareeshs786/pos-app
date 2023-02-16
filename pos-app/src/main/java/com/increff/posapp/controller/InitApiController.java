//package com.increff.posapp.controller;
//
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.increff.posapp.model.InfoData;
//import com.increff.posapp.model.UserForm;
//import com.increff.posapp.pojo.UserPojo;
//import com.increff.posapp.service.ApiException;
//import com.increff.posapp.service.UserService;
//import io.swagger.annotations.ApiOperation;
//
//@Controller
//public class InitApiController extends AbstractUiController {
//
//	@Autowired
//	private UserService service;
//	@Autowired
//	private InfoData info;
//	@Value("${app.supervisors}")
//	private String emails;
//
//
//
//	@ApiOperation(value = "Initializes application")
//	@RequestMapping(path = "/site/init", method = RequestMethod.GET)
//	public ModelAndView showPage(UserForm form) throws ApiException {
//		info.setMessage("Initializing application");
//		return mav("init.html");
//	}
//
//	@ApiOperation(value = "Initializes application")
//	@RequestMapping(path = "/site/init", method = RequestMethod.POST)
//	public ModelAndView initSite(UserForm form) throws ApiException {
//		List<UserPojo> list = service.getAll();
//		if (list.size() > 0) {
//			info.setMessage("Application already initialized. Please use existing credentials");
//		} else {
//			UserPojo p = convert(form);
//			String[] emailArray = emails.split(",");
//			Set<String> emailSet = new HashSet<>(Arrays.asList(emailArray));
//			if(emailSet.contains(form.getEmail()))
//				p.setRole("supervisor");
//			else
//				p.setRole("operator");
//			service.add(p);
//			info.setMessage("Application initialized");
//		}
//		return mav("init.html");
//
//	}
//
//	private static UserPojo convert(UserForm f) {
//		UserPojo p = new UserPojo();
//		p.setEmail(f.getEmail());
//		p.setPassword(f.getPassword());
//		return p;
//	}
//
//}
