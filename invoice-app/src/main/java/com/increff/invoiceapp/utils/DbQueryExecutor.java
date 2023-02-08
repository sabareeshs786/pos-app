package com.increff.invoiceapp.utils;


import java.sql.SQLOutput;

public class DbQueryExecutor {
    public static void main(String[] args){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                sb.append('a');
            } catch (Exception exception) {
                System.out.println(exception);
            }
        }
        System.out.println("Length: "+sb.toString().length());
    }
}
