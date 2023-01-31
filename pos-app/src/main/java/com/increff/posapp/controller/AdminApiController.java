package com.increff.posapp.controller;

import java.sql.SQLOutput;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.posapp.model.UserData;
import com.increff.posapp.model.UserForm;
import com.increff.posapp.pojo.UserPojo;
import com.increff.posapp.service.ApiException;
import com.increff.posapp.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class AdminApiController {

	@Autowired
	private UserService service;
	@Value("${app.supervisors}")
	private String emails;


	@ApiOperation(value = "Adds a user")
	@RequestMapping(path = "/api/supervisor/user", method = RequestMethod.POST)
	public void addUser(@RequestBody UserForm form) throws ApiException {
		System.out.println("Emails>>"+emails);
		String[] emailArray = emails.split(",");
		Set<String> emailSet = new HashSet<>(Arrays.asList(emailArray));
		UserPojo p = convert(form);
		if(emailSet.contains(form.getEmail()))
			p.setRole("supervisor");
		else
			p.setRole("operator");
		service.add(p);
	}

	@ApiOperation(value = "Deletes a user")
	@RequestMapping(path = "/api/supervisor/user/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets list of all users")
	@RequestMapping(path = "/api/supervisor/user", method = RequestMethod.GET)
	public List<UserData> getAllUser() {
		List<UserPojo> list = service.getAll();
		List<UserData> list2 = new ArrayList<UserData>();
		for (UserPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	private static UserData convert(UserPojo p) {
		UserData d = new UserData();
		d.setEmail(p.getEmail());
		d.setRole(p.getRole());
		d.setId(p.getId());
		return d;
	}

	private static UserPojo convert(UserForm f) {
		UserPojo p = new UserPojo();
		p.setEmail(f.getEmail());
		p.setPassword(f.getPassword());
		return p;
	}
}