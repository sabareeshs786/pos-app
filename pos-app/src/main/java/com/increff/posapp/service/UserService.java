package com.increff.posapp.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.posapp.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.posapp.dao.UserDao;
import com.increff.posapp.pojo.UserPojo;

@Service
public class UserService {

	@Autowired
	private UserDao dao;

	@Transactional
	public UserPojo add(UserPojo p) throws ApiException {
		normalize(p);
		validate(p);
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists");
		}
		return (UserPojo) dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	@Transactional
	public List<UserPojo> getAll() {
		return dao.selectAll(UserPojo.class);
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	private static void normalize(UserPojo p) {
		p.setEmail(StringUtil.toLowerCase(p.getEmail()));
		p.setRole(StringUtil.toLowerCase(p.getRole()));
	}

	private void validate(UserPojo p) throws ApiException {
		if(p.getEmail() == null){
			throw new ApiException("Email can't be empty");
		}
		if(p.getPassword() == null){
			throw new ApiException("Password can't be empty");
		}
	}
}
