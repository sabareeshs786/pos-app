package com.increff.posapp.util;

import com.increff.posapp.model.*;
import com.increff.posapp.service.ApiException;

public class Normalizer {
    public static void brandFormNormalizer(BrandForm form) throws ApiException {
        form.setBrand(StringUtil.toLowerCase(form.getBrand()));
        form.setCategory(StringUtil.toLowerCase(form.getCategory()));
    }

    public static void productFormNormalizer(ProductForm form) throws ApiException {
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()));
        form.setBrand(StringUtil.toLowerCase(form.getBrand()));
        form.setCategory(StringUtil.toLowerCase(form.getCategory()));
        form.setName(StringUtil.toLowerCase(form.getName()));
        form.setMrp(DoubleUtil.round(form.getMrp(), 2));
    }

    public static void inventoryFormNormalizer(InventoryForm form) throws ApiException {
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()));
    }

    public static void orderItemEditFormNormalizer(OrderItemEditForm form) throws ApiException {
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()));
        form.setSellingPrice(DoubleUtil.round(form.getSellingPrice(), 2));
    }

}
