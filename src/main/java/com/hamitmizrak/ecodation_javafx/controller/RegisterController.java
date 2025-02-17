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

    // Field (Veri tabanı işlemleri için)
    private UserDAO userDAO = new UserDAO();

    ///////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    /////////////////////////////////////////////////////////////////////////////////////////
    // ShowAlert
    /// //////////////////////////////////////////////////////////////////////////////////////
    // ShowAlert (Kullanıcıya bilgi veya hata mesajları göstermek için kullanılan yardımcı metot)
    private void showAlert(String title, String message, Alert.AlertType type) {
        // Alert nesnesi oluşturuyoruz ve parametre olarak alınan başlık, mesaj ve tipi ayarlıyoruz
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);

        // Alert penceresini gösteriyoruz ve kullanıcıdan bir yanıt bekliyoruz
        alert.showAndWait();
    } //end showAlert

    /////////////////////////////////////////////////////////////////////////////////////////
    // Switch
    private void switchToLogin() {
        try {
            // FXML Dosyalarını Yükle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/ecodation_javafx/view/login.fxml"));
            Parent loginParent = fxmlLoader.load();

            // Var olan sahneyi alıp ve değiştirmek
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loginParent));
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

        // KAyıt
        if (userDAO.registerUser(user)) {
            showAlert("Başarılı", "Kayıt Başarılı", Alert.AlertType.INFORMATION);

            // Kayıt başarılı ise Giriş(Login) Sayfaına yönlendirsin.
            switchToLogin();
        } else {
            showAlert("Hata", "Kayıt Başarısız.", Alert.AlertType.ERROR);
        }
    } // end register

} //end RegisterController
