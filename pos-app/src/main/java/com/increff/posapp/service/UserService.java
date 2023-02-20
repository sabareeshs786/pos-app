package com.increff.posapp.service;

import java.util.List;

import javax.transaction.Transactional;

import com.increff.posapp.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.posapp.dao.UserDao;
import com.increff.posapp.pojo.UserPojo;

@Service
@Transactional(rollbackOn = ApiException.class)
public class UserService {

	@Autowired
	private UserDao dao;

	public UserPojo add(UserPojo p) throws ApiException {
		normalize(p);
		validate(p);
		UserPojo existing = dao.select(p.getEmail());
		if (existing != null) {
			throw new ApiException("User with given email already exists");
		}
		return (UserPojo) dao.insert(p);
	}

	public UserPojo get(String email) throws ApiException {
		return dao.select(email);
	}

	public List<UserPojo> getAll() {
		return dao.selectAll(UserPojo.class);
	}

	public void delete(int id) {
		dao.delete(id);
	}

	public UserPojo update(Integer id, UserPojo p){
		UserPojo ex = dao.select(id);
		ex.setEmail(p.getEmail());
		ex.setPassword(p.getPassword());
		ex.setRole(p.getRole());
		dao.update(ex);
		return ex;
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
