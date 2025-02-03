package com.hamitmizrak.ecodation_javafx.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/user_management";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "rootroot";

    // import java.sql.Connection;
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("Veri tabanınan bağlanılmadı");
        }
    } //end getConnection
}
