package com.example.gest_patient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static String DB_Url = "jdbc:mysql://localhost:3306/gest_patient?user=root&password=";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_Url);
    }
}