package com.hamitmizrak.ecodation_javafx.dao;

import com.hamitmizrak.ecodation_javafx.database.SingletonDBConnection;

import java.sql.Connection;
import java.util.ArrayList;

public interface IDaoImplements<T> {
    // CRUD
    public boolean create(T t);

    public boolean update(T t);

    public boolean delete(int dataId);

    public ArrayList<T> list();

    // Gövdeli Method
    default Connection interfaceDatabaseConnection() {
        return SingletonDBConnection.getConnection();
    }
}
