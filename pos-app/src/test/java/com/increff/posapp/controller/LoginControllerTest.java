package com.increff.posapp.controller;

import com.increff.posapp.model.LoginForm;
import com.increff.posapp.service.AbstractUnitTest;
import com.increff.posapp.service.ApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class LoginControllerTest extends AbstractUnitTest {

    @Autowired
    private LoginController loginController;

    @Test
    public void testLoginSupervisor() throws ApiException {
        HttpServletRequest request = new MockHttpServletRequest();
        LoginForm form = new LoginForm();
        form.setEmail("hari@gmail.com");
        form.setPassword("1234");
        assertNotNull(loginController.login(request, form));
    }

    @Test
    public void testLoginOperator() throws ApiException {
        HttpServletRequest request = new MockHttpServletRequest();
        LoginForm form = new LoginForm();
        form.setEmail("user1@gmail.com");
        form.setPassword("12345");
        assertNotNull(loginController.login(request, form));
    }
}
