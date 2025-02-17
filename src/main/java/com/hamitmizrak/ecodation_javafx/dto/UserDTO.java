package com.hamitmizrak.ecodation_javafx.dto;

import lombok.*;

// LOMBOK
//@Getter
//@Setter
//@AllArgsConstructor // parametreli constructor
//@NoArgsConstructor  // parametresiz constructor
@ToString
@Builder
// UserDTO
public class UserDTO {

    // Field
    private int id;
    private String username;
    private String password;
    private String email;

    // Method
    // Parametresiz Constructor
    public UserDTO() {
    }

    // Parametreli Constructor
    public UserDTO(int id, String password, String email, String username) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.username = username;
    }

    // Getter And Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
} //end class
