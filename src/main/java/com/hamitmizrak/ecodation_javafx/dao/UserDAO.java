package com.hamitmizrak.ecodation_javafx.dao;

import com.hamitmizrak.ecodation_javafx.database.DBConnection;
import com.hamitmizrak.ecodation_javafx.dto.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// UserDAO
public class UserDAO {

    // import java.sql.Connection;
    private Connection connection;

    // Constructor
    public UserDAO() {
        this.connection = DBConnection.getConnection();
    }

    ///////////////////////////////////////////////////////////////
    // REGISTER (KAYIT)
    public boolean registerUser(UserDTO userDTO) {
        String sql = "INSERT INTO users(username,password,email) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userDTO.getUsername());
            preparedStatement.setString(2, userDTO.getPassword());
            preparedStatement.setString(3, userDTO.getEmail());
            // database kayıt
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }


    ///////////////////////////////////////////////////////////////
    // LOGIN
    public UserDTO loginUser(String username, String password) {
        String sql = "SELECT  *  FROM users WHERE username=? AND password=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // Database Kayıt almak için
            ResultSet resultSet = preparedStatement.executeQuery();
            // Eğer kullanıcı varsa
            if (resultSet.next()) {
                return new UserDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email")
                ); //end return
            } //end if
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////
    // CRUD
    // LIST
    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            // Eğer kullanıcı varsa
            while (resultSet.next()) {
                users.add(new UserDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email")
                ));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
        return users;
    } //end list

    // UPDATE
    public boolean updateUser(UserDTO userDTO) {
        String sql = "UPDATE users SET username=? password=?, email=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userDTO.getUsername());
            preparedStatement.setString(2, userDTO.getPassword());
            preparedStatement.setString(3, userDTO.getEmail());
            // database kayıt
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    } // end update


    // DELETE
    public boolean deleteUser(UserDTO userDTO) {
        String sql = "DELETE FROM users  WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userDTO.getId());
            // database kayıt
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    } //end Delete

} // end UserDAO
