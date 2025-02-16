package com.hamitmizrak.vatcalculation.controller;

import com.hamitmizrak.vatcalculation.dao.UserDAO;
import com.hamitmizrak.vatcalculation.dto.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    // Kullanıcı adını girmek için kullanılan TextField bileşeni
    /*
    @FXML, JavaFX uygulamalarında kullanılan bir annotation (notasyon) olarak, bir sınıfın içinde bulunan field, metot veya constructor'un, FXML dosyasındaki bileşenler ya da olay işleyiciler ile bağlanmasını sağlar.
     */
    @FXML
    private TextField usernameField;

    // Şifreyi girmek için kullanılan PasswordField bileşeni
    @FXML
    private PasswordField passwordField;

    // Veritabanı işlemleri için kullanılan UserDAO sınıfından bir nesne
    private UserDAO userDAO = new UserDAO();

    // Kullanıcı giriş işlemini gerçekleştiren metot
    @FXML
    private void login() {
        // TextField ve PasswordField'den kullanıcı adı ve şifre bilgilerini alıyoruz
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Kullanıcı bilgilerini doğrulamak için UserDAO sınıfının loginUser() metodunu çağırıyoruz
        UserDTO user = userDAO.loginUser(username, password);

        // Eğer kullanıcı bilgileri doğruysa
        if (user != null) {
            // Başarılı bir giriş mesajı gösteriyoruz
            showAlert("Başarılı", "Giriş başarılı!", Alert.AlertType.INFORMATION);

            // Admin paneline geçiş yapıyoruz
            openAdminPanel();
        } else {
            // Eğer bilgiler yanlışsa, hata mesajı gösteriyoruz
            showAlert("Hata", "Kullanıcı adı veya şifre yanlış.", Alert.AlertType.ERROR);
        }
    }

    // Klavyeden Enter tuşuna basıldığında giriş işlemini tetikleyen metot
    @FXML
    private void handleKeyPress(KeyEvent event) {
        // Eğer basılan tuş ENTER ise
        if (event.getCode() == KeyCode.ENTER) {
            // Giriş yap metodunu çağırıyoruz
            login();
        }
    }

    // Kullanıcı "Kayıt Ol" butonuna tıkladığında kayıt ekranına geçiş yapan metot
    @FXML
    private void switchToRegister(ActionEvent actionEvent) {
        try {
            // Kayıt ekranının FXML dosyasını yüklüyoruz
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/vatcalculation/view/register.fxml"));
            Parent registerRoot = fxmlLoader.load();

            // Mevcut sahneyi alıyoruz ve yeni sahneyle değiştiriyoruz
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(registerRoot));

            // Yeni pencerenin başlığını "Kayıt Ol" olarak ayarlıyoruz
            stage.setTitle("Kayıt Ol");

            // Sahneyi gösteriyoruz
            stage.show();
        } catch (IOException e) {
            // Eğer bir hata oluşursa hata mesajını yazdırıyoruz
            e.printStackTrace();

            // Kullanıcıya kayıt ekranının yüklenemediğine dair hata mesajı gösteriyoruz
            showAlert("Hata", "Kayıt ekranı yüklenemedi.", Alert.AlertType.ERROR);
        }
    }

    // Kullanıcıya bilgi veya hata mesajları göstermek için kullanılan yardımcı metot
    private void showAlert(String title, String message, Alert.AlertType type) {
        // Alert nesnesi oluşturuyoruz ve parametre olarak alınan başlık, mesaj ve tipi ayarlıyoruz
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);

        // Alert penceresini gösteriyoruz ve kullanıcıdan bir yanıt bekliyoruz
        alert.showAndWait();
    }

    // Giriş başarılı olduğunda admin panelini açan metot
    private void openAdminPanel() {
        try {
            // Admin panelinin FXML dosyasını yüklüyoruz
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/vatcalculation/view/admin.fxml"));
            Parent adminRoot = fxmlLoader.load();

            // Mevcut pencerenin sahnesini alıyoruz ve admin sahnesiyle değiştiriyoruz
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(adminRoot));

            // Pencerenin başlığını "Admin Paneli" olarak ayarlıyoruz
            stage.setTitle("Admin Paneli");

            // Sahneyi gösteriyoruz
            stage.show();
        } catch (IOException e) {
            // Eğer bir hata oluşursa hata mesajını yazdırıyoruz
            e.printStackTrace();

            // Kullanıcıya admin panelinin açılamadığına dair bir hata mesajı gösteriyoruz
            showAlert("Hata", "Admin paneli açılamadı.", Alert.AlertType.ERROR);
        }
    }
}
