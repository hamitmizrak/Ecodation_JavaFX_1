package com.hamitmizrak.ecodation_javafx.database;

import com.hamitmizrak.ecodation_javafx.utils.SpecialColor;

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
    // H2DB BEllek yerine Disk Tabanlı Veritabanı Kullanımı
    private static final String URL = "jdbc:h2:./h2db/user_management" + "AUTO_SERVER=TRUE";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    // SQL ile Connection
    private static Connection connection;

    // Parametresiz Constructor (Access Modifier)
    // private yapıyoruz çünkü  dışarıdan bu class'a erişim olmasın yani new(instance) olmadan.
    private SingletonDBConnection() {
    }

    // import java.sql.Connection;
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println(SpecialColor.CYAN+"H2DB Database Disk Veritabanına başarıyla sağlandı."+SpecialColor.RESET);
            } catch (SQLException sql) {
                System.out.println(SpecialColor.RED+"H2DB Database Disk Veritabanına başarıyla bağlanılmadı."+SpecialColor.RESET);
                sql.printStackTrace();
                throw new RuntimeException("H2DB Database Veri tabanınan bağlanılmadı");
            }
        } else {
            try {
                if(connection.isClosed()){
                    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    System.out.println(SpecialColor.BLUE+"H2DB Disk Veritabanı yeniden oluşturuldu."+SpecialColor.RESET);
                }
            } catch (Exception e) {
                System.out.println(SpecialColor.BLUE+"Veri tabanınan bağlanılmadı"+SpecialColor.RESET);
                e.printStackTrace();
                System.out.println("H2DB Veri tabanınan bağlanılmadı");
                throw new RuntimeException("H2DB Veri tabanınan bağlanılmadı");
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
                System.out.println("Database Veritabanı bağlantısı kapatıldı");
                System.out.println(SpecialColor.YELLOW+"Database Veritabanı bağlantısı kapatıldı"+SpecialColor.RESET);
            } catch (SQLException e) {
                System.out.println(SpecialColor.RED+"Database Kapatılmadı"+SpecialColor.RESET);
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    } //end closeConnection
} //end SingletonDBConnection
