package com.increff.posapp.util;

import com.increff.posapp.model.*;
import com.increff.posapp.service.ApiException;

import java.lang.reflect.Field;

public class Normalizer {

    public static void inventoryFormNormalizer(InventoryForm form) throws ApiException {
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()));
    }

    public static void normalize(Object o) throws IllegalAccessException {
        Field[] fields = o.getClass().getDeclaredFields();
        for(Field field: fields){
            if(field.getType().getSimpleName().equals("String")){
                field.setAccessible(true);
                String val = (String) field.get(o);
                field.set(o, val.toLowerCase());
                field.setAccessible(false);
            }
            else if(field.getType().getSimpleName().equals("Double")){
                field.setAccessible(true);
                Double val = (Double) field.get(o);
                field.set(o, DoubleUtil.round(val, 2));
                field.setAccessible(false);
            }
        }
    }
}
