package com.increff.invoiceapp.utils;
import com.opencsv.CSVWriter;

import java.sql.*;

public class DbQueryExecutor {
//    private static Connection con;
//    private static CSVWriter csvWriter;
//    public static void main(String[] args) {
//        try {
//            con = DriverManager.getConnection(
//                    "jdbc:mysql://localhost/posapplication", "increff", "Password@1234");
//            System.out.println("Connected to database");
//        } catch (SQLException e) {
//            System.out.println("Failed to connect to database");
//            e.printStackTrace();
//        }
//        try {
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM inventory");
//            while (rs.next()) {
//                int colCount = rs.getMetaData().getColumnCount();
//                int i = 1;
//                String resultRow = "";
//                while(i <= colCount){
//                    resultRow += rs.getString(i) + " ";
//                    i += 1;
//                }
//                System.out.println(resultRow);
//            }
//        } catch (SQLException e) {
//            System.out.println("Failed to execute query");
//            e.printStackTrace();
//        }
//    }
}
