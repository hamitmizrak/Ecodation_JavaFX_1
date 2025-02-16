package com.hamitmizrak.vatcalculation.controller;

import com.hamitmizrak.vatcalculation.dao.UserDAO;
import com.hamitmizrak.vatcalculation.dto.UserDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void register() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        UserDTO user = new UserDTO(0, username, password, email);

        if (userDAO.registerUser(user)) {
            showAlert("Başarılı", "Kayıt başarılı!", Alert.AlertType.INFORMATION);
            switchToLogin(); // Kayıt başarılıysa giriş ekranına geç
        } else {
            showAlert("Hata", "Kayıt sırasında bir hata oluştu.", Alert.AlertType.ERROR);
        }
    }

    private void switchToLogin() {
        try {
            // FXML dosyasını yükleme
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/vatcalculation/view/login.fxml"));
            Parent loginRoot = fxmlLoader.load();

            // Mevcut sahneyi alma ve değiştirme
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Giriş Yap");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Hata", "Giriş ekranına geçiş sırasında bir hata oluştu.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
