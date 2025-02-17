package com.hamitmizrak.ecodation_javafx.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonDBConnection {

    // MYSQL (Singleton olmadan)
    /*
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
    */

    // H2DB (Singleton olandan)
    private static final String URL = "jdbc:h2:./database/user_management" + "AUTO_SERVER=TRUE";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    // SQL ile Connection
    private static Connection connection;

    // Parametresiz Constructor (Access Modifier)
    // private yapıyoruz çünkü  dışarıdan bu class'a erişim olmasın yani new(instance) olmadan.
    private SingletonDBConnection() {
    }

    // import java.sql.Connection;
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("H2 Disk Veritabanına başarıyla sağlandı.");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Veri tabanınan bağlanılmadı");
            }
        } else {
            try {
                if(connection.isClosed()){
                    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    System.out.println("H2 Disk Veritabanı yeniden oluşturuldu.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Veri tabanınan bağlanılmadı");
            }
        }
        return connection;
    } //end getConnection

    // Database Close Connection
    public static void closeConnection(){
        if(connection!=null){
            try{
                connection.close();
                connection=null;
                System.out.println("H2Db Veritabanı bağlantısı kapatıldı");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    } //end closeConnection
} //end SingletonDBConnection
