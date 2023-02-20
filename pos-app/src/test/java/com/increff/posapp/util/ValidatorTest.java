package com.increff.posapp.util;

import com.increff.posapp.model.OrderForm;
import com.increff.posapp.service.AbstractUnitTest;
import com.increff.posapp.service.ApiException;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.junit.Test;

public class ValidatorTest extends AbstractUnitTest {
    @Test(expected = ApiException.class)
    public void testOrderFormValidatorBarcodeNull() throws ApiException {
        OrderForm form = new OrderForm();
        form.getBarcodes().add(null);
        Validator.orderFormValidator(form);
    }

    @Test(expected = ApiException.class)
    public void testOrderFormValidatorIsAlNum() throws ApiException {
        OrderForm form = new OrderForm();
        form.getBarcodes().add("23/..886Req");
        Validator.orderFormValidator(form);
    }

    @Test(expected = ApiException.class)
    public void testOrderFormValidatorQuantityNull() throws ApiException {
        OrderForm form = new OrderForm();
        form.getBarcodes().add("asdf");
        form.getQuantities().add(null);
        Validator.orderFormValidator(form);
    }

    @Test(expected = ApiException.class)
    public void testOrderFormValidatorQuantityNegative() throws ApiException {
        OrderForm form = new OrderForm();
        form.getBarcodes().add("asdf");
        form.getQuantities().add(-9);
        Validator.orderFormValidator(form);
    }

    @Test(expected = ApiException.class)
    public void testOrderFormValidatorSpNull() throws ApiException {
        OrderForm form = new OrderForm();
        form.getBarcodes().add("asdf");
        form.getQuantities().add(5);
        form.getSellingPrices().add(null);
        Validator.orderFormValidator(form);
    }

    @Test(expected = ApiException.class)
    public void testOrderFormValidatorSpNegative() throws ApiException {
        OrderForm form = new OrderForm();
        form.getBarcodes().add("asdf");
        form.getQuantities().add(5);
        form.getSellingPrices().add(-8.9);
        Validator.orderFormValidator(form);
    }

    @Test(expected = ApiException.class)
    public void testOrderFormValidatorSpNan() throws ApiException {
        OrderForm form = new OrderForm();
        form.getBarcodes().add("asdf");
        form.getQuantities().add(5);
        form.getSellingPrices().add(Double.NaN);
        Validator.orderFormValidator(form);
    }

    @Test(expected = ApiException.class)
    public void testOrderFormValidatorSpInfinite() throws ApiException {
        OrderForm form = new OrderForm();
        form.getBarcodes().add("asdf");
        form.getQuantities().add(5);
        form.getSellingPrices().add(Double.POSITIVE_INFINITY);
        Validator.orderFormValidator(form);
    }

    @Test(expected = ApiException.class)
    public void testStringValidatorNull() throws ApiException {
        Validator.stringValidator("Field", null);
    }

    @Test(expected = ApiException.class)
    public void testStringValidatorEmpty() throws ApiException {
        Validator.stringValidator("Field", "");
    }


}
