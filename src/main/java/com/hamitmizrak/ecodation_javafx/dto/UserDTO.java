package com.hamitmizrak.ecodation_javafx.dto;

import lombok.*;

// LOMBOK
@Getter
@Setter
@AllArgsConstructor // parametreli constructor
@NoArgsConstructor //parametresiz constructor
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

    // Getter And Setter

}
