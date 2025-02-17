package com.hamitmizrak.ecodation_javafx.controller;

import com.hamitmizrak.ecodation_javafx.dao.UserDAO;
import com.hamitmizrak.ecodation_javafx.dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminController {

    // Database İşlemleri
    private com.hamitmizrak.ecodation_javafx.dao.UserDAO userDAO = new UserDAO();

    ///////////////////////////////////////////////////////////////////////////
    // User
    @FXML
    private TableView<UserDTO> userTable;

    @FXML
    private TableColumn<UserDTO, Integer> idColumn;

    @FXML
    private TableColumn<UserDTO, String> usernameColumn;

    @FXML
    private TableColumn<UserDTO, String> emailColumn;

    @FXML
    private TableColumn<UserDTO, String> passwordColumn; // Şifre sütunu

    ///////////////////////////////////////////////////////////////////////////
    @FXML
    public void initialize() {
        // Tablodaki sütunları bağlama
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Düz şifre
        // passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password")); // Şifre sütunu bağlama
        // Şifre sütununda maskeleme özelliğini aktif et
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordColumn.setCellFactory(column -> new TableCell<UserDTO, String>() {
            @Override
            protected void updateItem(String password, boolean empty) {
                super.updateItem(password, empty);
                if (empty || password == null) {
                    setText(null);
                } else {
                    setText("******"); // Şifreyi maskele
                }
            }
        });

        // Kullanıcı listesini yükleme
        refreshTable();
    }

    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void refreshTable() {
        List<UserDTO> users = userDAO.list();
        ObservableList<UserDTO> userObservableList = FXCollections.observableArrayList(users);
        userTable.setItems(userObservableList);
        showAlert("Bilgi", "Tablo başarıyla yenilendi!", Alert.AlertType.INFORMATION);
    }

    ///////////////////////////////////////////////////////////////////////////
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////
    // Logout
    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Çıkış Yap");
        alert.setHeaderText("Oturumdan çıkmak istiyor musunuz?");
        alert.setContentText("Emin misiniz?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/ecodation_javafx/view/Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) userTable.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
                //stage.close();
            } catch (IOException e) {
                showAlert("Hata", "Giriş sayfasına yönlendirme başarısız!", Alert.AlertType.ERROR);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void addUser() {
        // Yeni kullanıcı ekleme için bir dialog penceresi açıyoruz
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Yeni Kullanıcı Ekle");
        usernameDialog.setHeaderText("Yeni Kullanıcı Ekleme");
        usernameDialog.setContentText("Kullanıcı adı:");

        Optional<String> usernameResult = usernameDialog.showAndWait();
        // Text Input içinde veri girilmişse
        if (usernameResult.isPresent()) {
            String username = usernameResult.get();

            // Şifre için input
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Yeni Kullanıcı Ekle");
            passwordDialog.setHeaderText("Yeni Kullanıcı Ekleme");
            passwordDialog.setContentText("Şifre:");

            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (passwordResult.isPresent()) {
                String password = passwordResult.get();

                // E-posta için input
                TextInputDialog emailDialog = new TextInputDialog();
                emailDialog.setTitle("Yeni Kullanıcı Ekle");
                emailDialog.setHeaderText("Yeni Kullanıcı Ekleme");
                emailDialog.setContentText("E-posta:");

                Optional<String> emailResult = emailDialog.showAndWait();

                if (emailResult.isPresent()) {
                    String email = emailResult.get();

                    // Kullanıcıyı ekle
                    UserDTO newUser = new UserDTO(0, username, password, email);
                    if (userDAO.create(newUser)) {
                        showAlert("Başarılı", "Kullanıcı başarıyla eklendi!", Alert.AlertType.INFORMATION);
                        refreshTable();
                    } else {
                        showAlert("Hata", "Kullanıcı eklenirken bir hata oluştu.", Alert.AlertType.ERROR);
                    }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    @FXML
    private void updateUser() {
        // Seçilen kullanıcıyı güncelle
        UserDTO selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Kullanıcı adı için input
            TextInputDialog usernameDialog = new TextInputDialog(selectedUser.getUsername());
            usernameDialog.setTitle("Kullanıcı Güncelle");
            usernameDialog.setHeaderText("Kullanıcı Güncelleme");
            usernameDialog.setContentText("Yeni kullanıcı adı:");

            Optional<String> usernameResult = usernameDialog.showAndWait();

            if (usernameResult.isPresent()) {
                String username = usernameResult.get();

                // Şifre için input
                TextInputDialog passwordDialog = new TextInputDialog(selectedUser.getPassword());
                passwordDialog.setTitle("Kullanıcı Güncelle");
                passwordDialog.setHeaderText("Kullanıcı Güncelleme");
                passwordDialog.setContentText("Yeni şifre:");

                Optional<String> passwordResult = passwordDialog.showAndWait();

                if (passwordResult.isPresent()) {
                    String password = passwordResult.get();

                    // E-posta için input
                    TextInputDialog emailDialog = new TextInputDialog(selectedUser.getEmail());
                    emailDialog.setTitle("Kullanıcı Güncelle");
                    emailDialog.setHeaderText("Kullanıcı Güncelleme");
                    emailDialog.setContentText("Yeni e-posta:");

                    Optional<String> emailResult = emailDialog.showAndWait();

                    if (emailResult.isPresent()) {
                        String email = emailResult.get();

                        // Kullanıcıyı güncelle
                        selectedUser.setUsername(username);
                        selectedUser.setPassword(password);
                        selectedUser.setEmail(email);

                        if (userDAO.update(selectedUser)) {
                            showAlert("Başarılı", "Kullanıcı başarıyla güncellendi!", Alert.AlertType.INFORMATION);
                            refreshTable();
                        } else {
                            showAlert("Hata", "Kullanıcı güncellenirken bir hata oluştu.", Alert.AlertType.ERROR);
                        }
                    }
                }
            }
        } else {
            showAlert("Uyarı", "Lütfen bir kullanıcı seçin!", Alert.AlertType.WARNING);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private void deleteUser() {
        // Seçilen kullanıcıyı al
        UserDTO selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Onay penceresi
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Silme Onayı");
            confirmationAlert.setHeaderText("Kullanıcıyı silmek istiyor musunuz?");
            confirmationAlert.setContentText("Silinecek kullanıcı: " + selectedUser.getUsername());

            // Kullanıcı onayı al
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Silme işlemini gerçekleştir
                if (userDAO.delete(selectedUser.getId())) {
                    showAlert("Başarılı", "Kullanıcı başarıyla silindi!", Alert.AlertType.INFORMATION);
                    refreshTable();
                } else {
                    showAlert("Hata", "Kullanıcı silinirken bir hata oluştu.", Alert.AlertType.ERROR);
                }
            } else {
                // Silme işlemi iptal edildi
                showAlert("Bilgi", "Silme işlemi iptal edildi.", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Uyarı", "Lütfen bir kullanıcı seçin!", Alert.AlertType.WARNING);
        }
    } // end delete

} //end class
