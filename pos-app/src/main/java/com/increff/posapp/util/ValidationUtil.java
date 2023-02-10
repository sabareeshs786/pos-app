package com.increff.posapp.util;

public class ValidationUtil {
    public static Boolean isPageSizeValid(Integer page, Integer size){
        return page != null && size != null;
    }
}
