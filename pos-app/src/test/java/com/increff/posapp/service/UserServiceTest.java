package com.increff.posapp.service;

import com.increff.posapp.pojo.UserPojo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class UserServiceTest extends AbstractUnitTest {

    @Autowired
    private UserService userService;

    private UserPojo addUser() throws ApiException {
        UserPojo p = new UserPojo();
        p.setEmail("raj@gmail.com");
        p.setRole("supervisor");
        p.setPassword("1234");
        return userService.add(p);
    }
    @Test
    public void testNormalizeEmail() throws ApiException {
        UserPojo p = new UserPojo();
        p.setEmail(" RaJ@Gmail.Com");
        p.setRole("supervisor");
        p.setPassword("1234");
        userService.add(p);
    }

    @Test
    public void testNormalizeRole() throws ApiException {
        UserPojo p = new UserPojo();
        p.setEmail("raj@gmail.com");
        p.setRole(" SupervIsoR");
        p.setPassword("1234");
        userService.add(p);
    }

    @Test(expected = ApiException.class)
    public void testValidateEmailNull() throws ApiException {
        UserPojo p = new UserPojo();
        p.setEmail(null);
        p.setRole(" SupervIsoR");
        p.setPassword("1234");
        userService.add(p);
    }

    @Test(expected = ApiException.class)
    public void testValidatePasswordNull() throws ApiException {
        UserPojo p = new UserPojo();
        p.setEmail("raj@gmail.com");
        p.setRole(" SupervIsoR");
        p.setPassword(null);
        userService.add(p);
    }

    @Test(expected = ApiException.class)
    public void testAddUserDuplicate() throws ApiException {
        addUser();
        UserPojo p = new UserPojo();
        p.setEmail("raj@gmail.com");
        p.setRole("supervisor");
        p.setPassword("1234");
        userService.add(p);
    }

    @Test
    public void testGet() throws ApiException {
        addUser();
        UserPojo pojo = userService.get("raj@gmail.com");
        assertEquals("raj@gmail.com", pojo.getEmail());
        assertEquals("supervisor", pojo.getRole());
        assertEquals("1234", pojo.getPassword());
    }

    @Test
    public void testGetAll() throws ApiException {
        addUser();
        List<UserPojo> userPojoList = userService.getAllInPage(0, 5);
        assertTrue(userPojoList.size() > 0);
    }

    @Test
    public void testDelete() throws ApiException {
        UserPojo pojo1 = addUser();
        userService.delete(pojo1.getId());
        assertNull(userService.get("raj@gmail.com"));
    }

    @Test
    public void testUpdate() throws ApiException {
        UserPojo pojo1 = addUser();
        UserPojo pojo = new UserPojo();
        pojo.setEmail("user2@gmail.com");
        pojo.setPassword("12345");
        pojo.setRole("supervisor");
        UserPojo pojo2 = userService.update(pojo1.getId(), pojo);
        assertEquals("user2@gmail.com", pojo2.getEmail());
        assertEquals("12345", pojo2.getPassword());
        assertEquals("supervisor", pojo2.getRole());
    }

}
