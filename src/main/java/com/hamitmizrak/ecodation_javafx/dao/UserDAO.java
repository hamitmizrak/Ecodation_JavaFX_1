package com.hamitmizrak.ecodation_javafx.dao;


import com.hamitmizrak.ecodation_javafx.database.SingletonDBConnection;
import com.hamitmizrak.ecodation_javafx.dto.UserDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserDAO implements IDaoImplements<UserDTO> {
    // Connection Database
    private Connection connection;

    // Parametresiz Constructor
    public UserDAO() {
        this.connection = SingletonDBConnection.getConnection();
    } // Singleton bağlantısını kullan

    /// ////////////////////////////////////////////
    // CRUD
    // CREATE
    public boolean create(UserDTO userDTO) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userDTO.getUsername());
            statement.setString(2, userDTO.getPassword()); // Şifreleme eklenebilir (örn: BCrypt)
            statement.setString(3, userDTO.getEmail());
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // LIST
    public ArrayList<UserDTO> list() {
        ArrayList<UserDTO> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new UserDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    // UPDATE
    public boolean update(UserDTO user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean delete(int dataId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, dataId);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /// /////////////////////////////////////////
    /// // Login
    public UserDTO loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password); // Güvenlik için hashleme yapılabilir
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new UserDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

} //end UserDAO
