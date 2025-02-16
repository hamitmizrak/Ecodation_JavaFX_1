package com.hamitmizrak.vatcalculation.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonDBConnection {
    // Bellek yerine DISK TABANLI veritabanı kullanımı
    private static final String JDBC_URL = "jdbc:h2:./database/user_management;" +
            "AUTO_SERVER=TRUE";
    //    private static final String JDBC_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL"; //
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Connection connection;

    private SingletonDBConnection() {
        // Private constructor (dışarıdan erişilmesini engeller)
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                System.out.println("H2 Disk Veritabanı bağlantısı kuruldu.");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Veritabanı bağlantısı kurulamadı.");
            }
        } else {
            try {
                if (connection.isClosed()) {
                    connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                    System.out.println("H2 Disk Veritabanı yeniden oluşturuldu.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Veritabanı bağlantısı kapatıldı.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
