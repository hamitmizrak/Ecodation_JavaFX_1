package com.hamitmizrak.ecodation_javafx.controller;

import com.hamitmizrak.ecodation_javafx.dao.UserDAO;
import com.hamitmizrak.ecodation_javafx.dto.UserDTO;
import com.hamitmizrak.ecodation_javafx.utils.SpecialColor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.SQLException;

// import javafx.scene.control.TextField;
// FXML dosyasındaki bileşeni Controller sınıfdındaki değişkene bağlar.
/*
    @FXML, JavaFX uygulamalarında kullanılan bir annotation (notasyon) olarak, bir sınıfın içinde bulunan field, metot veya constructor'un, FXML dosyasındaki bileşenler ya da olay işleyiciler ile bağlanmasını sağlar.
*/
public class LoginController {

    // Field (Veri tabanı işlemleri için)
    private UserDAO userDAO = new UserDAO();

    /// //////////////////////////////////////////////////////////////////////////////////////
    // FXML Field
    @FXML
    private TextField usernameField;

    // Şifreyi girmek için kullanılan PasswordField bileşeni
    @FXML
    private TextField passwordField;

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

    /// //////////////////////////////////////////////////////////////////////////////////////
    // Klavyeden ENTER tuşuna bastığımda giriş yapsın
    @FXML
    private void specialOnEnterPressed(KeyEvent keyEvent) {
        // Eğer basılan tuş ENTER ise
        if (keyEvent.getCode() == KeyCode.ENTER) {
            // Eğer Enter'a basarsam login() sayfasına gitsin
            login();
        }
    } // onEnterPressed

    ////////////////////////////////////////////////////////////////////////////////////////
    // Login ( Kullanıcı giriş işlemini gerçekleştiren metot)
    @FXML
    public void login() {
        // Kullanıcı girişi yaparken username, password
        String username = usernameField.getText();
        String password = passwordField.getText();

        // UserDTO (// Kullanıcı bilgilerini doğrulamak için UserDAO sınıfının loginUser() metodunu çağırıyoruz)
        UserDTO user = userDAO.loginUser(username, password);

        // Eğer kullanıcı varsa,Eğer kullanıcı bilgileri doğruysa
        if (user != null) {
            // Başarılı bir giriş mesajı gösteriyoruz
            showAlert("Başarılı", "Giriş Başarılı", Alert.AlertType.INFORMATION);

            // Kayıt başarılı ise Admin paneline geçiş yapıyoruz
            openAdminPane();
        } else {
            // Eğer bilgiler yanlışsa, hata mesajı gösteriyoruz
            showAlert("Hata", "Kayıt adı veya şifre yanlış.", Alert.AlertType.ERROR);
        }
    } // end login

    ////////////////////////////////////////////////////////////////////////////////////////
    // Eğer Giriş başarılı ise Admin  paneline Geçiş yapalım
    private void openAdminPane() {
        try {
            //  Admin panelinin FXML dosyasını yüklüyoruz
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/ecodation_javafx/view/admin.fxml"));
            Parent parent = fxmlLoader.load();

            // Mevcut pencerenin sahnesini alıyoruz ve admin sahnesiyle değiştiriyoruz
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(parent));

            // Pencerenin başlığını "Admin Paneli" olarak ayarlıyoruz
            stage.setTitle("Admin Paneli: " + usernameField);

            // Sahneyi gösteriyoruz
            stage.show();
        } catch (Exception e) {
            //throw new RuntimeException(e);
            System.out.println(SpecialColor.RED+"Admin Sayfası Yüklenemedi"+SpecialColor.RESET);
            e.printStackTrace();
            showAlert("Hata", "Admin Sayfası Yüklenemedi", Alert.AlertType.ERROR);
        }
    }

    /// //////////////////////////////////////////////////////////////////////////////////////
    // Register (Switch)
    @FXML
    private void switchToRegister(ActionEvent actionEvent) {
        try {
            // FXML Dosyalarını Yükle (Kayıt ekranının FXML dosyasını yüklüyoruz)
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/ecodation_javafx/view/register.fxml"));
            Parent parent = fxmlLoader.load();

            // Var olan sahneyi alıp ve değiştirmek
            Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Kayıt Ol");
            stage.show();

        } catch (Exception e) {
            //throw new RuntimeException(e);
            System.out.println(SpecialColor.RED + "Register Sayfasında yönlendirilmedi" + SpecialColor.RESET);
            e.printStackTrace();
            showAlert("Hata", "Kayıt Ekranı Yüklenemedi", Alert.AlertType.ERROR);
        }
    } //end switchToLogin

}  // end LoginController
