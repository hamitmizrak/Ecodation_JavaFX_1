package com.hamitmizrak.vatcalculation.controller;

import com.hamitmizrak.vatcalculation.dao.ReceiptsDAO;
import com.hamitmizrak.vatcalculation.dao.UserDAO;
import com.hamitmizrak.vatcalculation.dto.ReceiptsDTO;
import com.hamitmizrak.vatcalculation.dto.UserDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AdminController {
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

    private UserDAO userDAO = new UserDAO();

    @FXML
    private TableView<ReceiptsDTO> receiptTable;

    @FXML
    private TableColumn<ReceiptsDTO, String> receiptNumberColumn;

    @FXML
    private TableColumn<ReceiptsDTO, String> companyNameColumn;

    @FXML
    private TableColumn<ReceiptsDTO, String> customerNameColumn;

    @FXML
    private TableColumn<ReceiptsDTO, Double> amountColumn;

    private ReceiptsDAO receiptsDAO = new ReceiptsDAO();

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

    /// /////////////////////////////////////////////////////////////////////////////////////////////////
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private void refreshTable() {
        List<UserDTO> users = userDAO.getAllUsers();
        ObservableList<UserDTO> userObservableList = FXCollections.observableArrayList(users);
        userTable.setItems(userObservableList);

        showAlert("Bilgi", "Tablo başarıyla yenilendi!", Alert.AlertType.INFORMATION);
    }


    /// /////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private void addUser() {
        // Yeni kullanıcı ekleme için bir dialog penceresi açıyoruz
        TextInputDialog usernameDialog = new TextInputDialog();
        usernameDialog.setTitle("Yeni Kullanıcı Ekle");
        usernameDialog.setHeaderText("Yeni Kullanıcı Ekleme");
        usernameDialog.setContentText("Kullanıcı adı:");

        Optional<String> usernameResult = usernameDialog.showAndWait();

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
                    if (userDAO.registerUser(newUser)) {
                        showAlert("Başarılı", "Kullanıcı başarıyla eklendi!", Alert.AlertType.INFORMATION);
                        refreshTable();
                    } else {
                        showAlert("Hata", "Kullanıcı eklenirken bir hata oluştu.", Alert.AlertType.ERROR);
                    }
                }
            }
        }
    }


    /// /////////////////////////////////////////////////////////////////////////////////////////////////
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

                        if (userDAO.updateUser(selectedUser)) {
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

    /// /////////////////////////////////////////////////////////////////////////////////////////////////
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
                if (userDAO.deleteUser(selectedUser.getId())) {
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
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private void calculateVAT() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("KDV Hesaplama");
        dialog.setHeaderText("KDV Dahil Fiyat Hesaplama");
        dialog.setContentText("Fiyatı girin:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(priceStr -> {
            try {
                double price = Double.parseDouble(priceStr);
                double vatRate = 0.18;
                double priceWithVAT = price + (price * vatRate);
                showAlert("Hesaplama Sonucu", "KDV'li Fiyat: " + priceWithVAT, Alert.AlertType.INFORMATION);
            } catch (NumberFormatException e) {
                showAlert("Hata", "Geçerli bir sayı girin!", Alert.AlertType.ERROR);
            }
        });
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Çıkış Yap");
        alert.setHeaderText("Oturumdan çıkmak istiyor musunuz?");
        alert.setContentText("Emin misiniz?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/vatcalculation/view/Login.fxml"));
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

    /// /////////////////////////////////////////////////////////////////////////////////////////////////
    ///
    @FXML
    private void showReceiptManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hamitmizrak/vatcalculation/view/receipt.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Fiş Yönetimi");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Hata", "Fiş yönetim ekranı açılamadı!", Alert.AlertType.ERROR);
        }
    }

    /// /////////////////////////////////////////////////////////////////////////////////////////////////


    @FXML
    private void addReceipt() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Fiş Ekle");
        dialog.setHeaderText("Yeni Muhasebe Fişi Ekle");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField receiptNumber = new TextField();
        receiptNumber.setPromptText("Fiş Numarası");

        DatePicker receiptDate = new DatePicker();

        TextField taxNumber = new TextField();
        taxNumber.setPromptText("Vergi Numarası");

        TextField companyName = new TextField();
        companyName.setPromptText("Firma Adı");

        TextField customerName = new TextField();
        customerName.setPromptText("Müşteri Adı");

        TextField description = new TextField();
        description.setPromptText("Açıklama");

        TextField createdBy = new TextField();
        createdBy.setPromptText("Düzenleyen Kişi");

        TextField accountCode = new TextField();
        accountCode.setPromptText("Hesap Kodu");

        ComboBox<String> receiptType = new ComboBox<>();
        receiptType.getItems().addAll("Ödeme", "Tahsilat", "Masraf", "Gelir");

        TextField amount = new TextField();
        amount.setPromptText("Tutar");

        TextField vatRate = new TextField();
        vatRate.setPromptText("KDV Oranı");

        TextField totalAmount = new TextField();
        totalAmount.setPromptText("KDV Dahil Tutar");

        ComboBox<String> paymentType = new ComboBox<>();
        paymentType.getItems().addAll("Nakit", "Kredi Kartı", "Havale", "Çek");

        grid.add(new Label("Fiş Numarası:"), 0, 0);
        grid.add(receiptNumber, 1, 0);
        grid.add(new Label("Fiş Tarihi:"), 0, 1);
        grid.add(receiptDate, 1, 1);
        grid.add(new Label("Vergi Numarası:"), 0, 2);
        grid.add(taxNumber, 1, 2);
        grid.add(new Label("Firma Adı:"), 0, 3);
        grid.add(companyName, 1, 3);
        grid.add(new Label("Müşteri Adı:"), 0, 4);
        grid.add(customerName, 1, 4);
        grid.add(new Label("Açıklama:"), 0, 5);
        grid.add(description, 1, 5);
        grid.add(new Label("Düzenleyen Kişi:"), 0, 6);
        grid.add(createdBy, 1, 6);
        grid.add(new Label("Hesap Kodu:"), 0, 7);
        grid.add(accountCode, 1, 7);
        grid.add(new Label("Fiş Türü:"), 0, 8);
        grid.add(receiptType, 1, 8);
        grid.add(new Label("Tutar:"), 0, 9);
        grid.add(amount, 1, 9);
        grid.add(new Label("KDV Oranı:"), 0, 10);
        grid.add(vatRate, 1, 10);
        grid.add(new Label("KDV Dahil Tutar:"), 0, 11);
        grid.add(totalAmount, 1, 11);
        grid.add(new Label("Ödeme Türü:"), 0, 12);
        grid.add(paymentType, 1, 12);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            ReceiptsDTO receipt = new ReceiptsDTO(
                    0,
                    receiptNumber.getText(),
                    receiptDate.getValue(),
                    taxNumber.getText(),
                    companyName.getText(),
                    customerName.getText(),
                    description.getText(),
                    createdBy.getText(),
                    accountCode.getText(),
                    receiptType.getValue(),
                    Double.parseDouble(amount.getText()),
                    Double.parseDouble(vatRate.getText()),
                    Double.parseDouble(totalAmount.getText()),
                    paymentType.getValue()
            );
            if (receiptsDAO.addReceipt(receipt)) {
                showAlert("Başarılı", "Fiş başarıyla kaydedildi!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Hata", "Fiş eklenirken hata oluştu.", Alert.AlertType.ERROR);
            }
        });

    }

}
