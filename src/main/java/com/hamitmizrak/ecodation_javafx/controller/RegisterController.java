package com.hamitmizrak.ecodation_javafx.controller;

import com.hamitmizrak.ecodation_javafx.dao.UserDAO;
import com.hamitmizrak.ecodation_javafx.dto.UserDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/*
RegisterController ile  resources/paketadi/register.fxml
register.fxml ile ekran oluşturacağım.
 */

public class RegisterController {

    // import javafx.scene.control.TextField;
    // FXML dosyasındaki bileşeni Controller sınıfdındaki değişkene bağlar.
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    // Field
    private UserDAO userDAO = new UserDAO();

    /////////////////////////////////////////////////////////////////////////////////////////
    // ShowAlert
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    } //end showAlert

    /////////////////////////////////////////////////////////////////////////////////////////
    // Switch
    @FXML
    private void switchToLogin() {
        try {
            // FXML Dosyalarını Yükle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/ecodation_javafx/login.fxml"));
            Parent parent = fxmlLoader.load();

            // Var olan sahneyi alıp ve değiştirmek
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Giriş Yap");
            stage.show();

        } catch (Exception e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            showAlert("Hata", "Giriş Ekranına Geçiş Sırasında Hata meydana geldi", Alert.AlertType.ERROR);
        }
    } //end switchToLogin


    /////////////////////////////////////////////////////////////////////////////////////////
    // Register
    @FXML
    public void register() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // UserDTO
        UserDTO user = new UserDTO(0, username, password, email);
        if (userDAO.registerUser(user)) {
            showAlert("Başarılı", "Kayıt Başarılı", Alert.AlertType.INFORMATION);

            // Kayıt başarılı ise Giriş(Login) Sayfaına yönlendirsin.
            switchToLogin();
        } else {
            showAlert("Hata", "Kayıt Başarısız.", Alert.AlertType.ERROR);
        }
    } // end register

} //end RegisterController
