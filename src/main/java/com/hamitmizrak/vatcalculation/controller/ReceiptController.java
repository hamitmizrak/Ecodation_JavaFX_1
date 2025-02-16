package com.hamitmizrak.vatcalculation.controller;

import com.hamitmizrak.vatcalculation.dao.ReceiptsDAO;
import com.hamitmizrak.vatcalculation.dto.ReceiptsDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Cell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ReceiptController {
    @FXML private TableView<ReceiptsDTO> receiptTable;
    @FXML private TableColumn<ReceiptsDTO, String> receiptNumberColumn;
    @FXML private TableColumn<ReceiptsDTO, LocalDate> receiptDateColumn;
    @FXML private TableColumn<ReceiptsDTO, String> taxNumberColumn;
    @FXML private TableColumn<ReceiptsDTO, String> companyNameColumn;
    @FXML private TableColumn<ReceiptsDTO, String> customerNameColumn;
    @FXML private TableColumn<ReceiptsDTO, String> descriptionColumn;
    @FXML private TableColumn<ReceiptsDTO, String> createdByColumn;
    @FXML private TableColumn<ReceiptsDTO, String> accountCodeColumn;
    @FXML private TableColumn<ReceiptsDTO, String> receiptTypeColumn;
    @FXML private TableColumn<ReceiptsDTO, Double> amountColumn;
    @FXML private TableColumn<ReceiptsDTO, Double> vatRateColumn;
    @FXML private TableColumn<ReceiptsDTO, Double> totalAmountColumn;
    @FXML private TableColumn<ReceiptsDTO, String> paymentTypeColumn;

    private ReceiptsDAO receiptsDAO = new ReceiptsDAO();

    @FXML
    public void initialize() {
        receiptNumberColumn.setCellValueFactory(new PropertyValueFactory<>("receiptNumber"));
        receiptDateColumn.setCellValueFactory(new PropertyValueFactory<>("receiptDate"));
        taxNumberColumn.setCellValueFactory(new PropertyValueFactory<>("taxNumber"));
        companyNameColumn.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        accountCodeColumn.setCellValueFactory(new PropertyValueFactory<>("accountCode"));
        receiptTypeColumn.setCellValueFactory(new PropertyValueFactory<>("receiptType"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        vatRateColumn.setCellValueFactory(new PropertyValueFactory<>("vatRate"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));

        refreshReceiptsTable();
    }

    @FXML
    private void refreshReceiptsTable() {
        List<ReceiptsDTO> receipts = receiptsDAO.getAllReceipts();
        ObservableList<ReceiptsDTO> receiptsObservableList = FXCollections.observableArrayList(receipts);
        receiptTable.setItems(receiptsObservableList);
    }

    @FXML
    private void addReceipt() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Fiş Ekle");
        dialog.setHeaderText("Yeni Muhasebe Fişi Ekle");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // **Giriş Alanları**
        TextField receiptNumber = new TextField();
        receiptNumber.setPromptText("Örn: 12345"); // ✅ Placeholder

        DatePicker receiptDate = new DatePicker();
        receiptDate.setPromptText("Tarih seçin"); // ✅ Placeholder

        TextField taxNumber = new TextField();
        taxNumber.setPromptText("Örn: 1234567890"); // ✅ Placeholder

        TextField companyName = new TextField();
        companyName.setPromptText("Örn: XYZ Şirketi A.Ş."); // ✅ Placeholder

        TextField customerName = new TextField();
        customerName.setPromptText("Örn: Ali Veli"); // ✅ Placeholder

        TextField description = new TextField();
        description.setPromptText("Örn: Ürün satışı"); // ✅ Placeholder

        TextField createdBy = new TextField();
        createdBy.setPromptText("Örn: Ahmet Yılmaz"); // ✅ Placeholder

        TextField accountCode = new TextField();
        accountCode.setPromptText("Örn: 600-01"); // ✅ Placeholder

        TextField amount = new TextField();
        amount.setPromptText("Örn: 1000.50"); // ✅ Placeholder

        TextField vatRate = new TextField("18"); // Varsayılan KDV %18
        vatRate.setPromptText("Örn: 18 (KDV % cinsinden)"); // ✅ Placeholder

        TextField totalAmount = new TextField();
        totalAmount.setEditable(false); // **Kullanıcı değiştiremesin**
        totalAmount.setPromptText("Otomatik hesaplanır"); // ✅ Placeholder

        // **Seçim Kutuları (ComboBox)**
        ComboBox<String> receiptType = new ComboBox<>();
        receiptType.getItems().addAll("Ödeme", "Tahsilat", "Masraf", "Gelir");
        receiptType.setPromptText("Fiş türü seçin"); // ✅ Placeholder

        ComboBox<String> paymentType = new ComboBox<>();
        paymentType.getItems().addAll("Nakit", "Kredi Kartı", "Havale", "Çek");
        paymentType.setPromptText("Ödeme türü seçin"); // ✅ Placeholder

        // **Otomatik KDV Dahil Hesaplama**
        amount.textProperty().addListener((obs, oldVal, newVal) -> updateTotalAmount(amount, vatRate, totalAmount));
        vatRate.textProperty().addListener((obs, oldVal, newVal) -> updateTotalAmount(amount, vatRate, totalAmount));

        // **GridPane’e Alanları Ekle**
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
        grid.add(new Label("KDV Oranı (%):"), 0, 10);
        grid.add(vatRate, 1, 10);
        grid.add(new Label("KDV Dahil Tutar:"), 0, 11);
        grid.add(totalAmount, 1, 11);
        grid.add(new Label("Ödeme Türü:"), 0, 12);
        grid.add(paymentType, 1, 12);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            try {
                // **Boş Alan Kontrolü**
                if (receiptNumber.getText().isEmpty() || taxNumber.getText().isEmpty() ||
                        companyName.getText().isEmpty() || customerName.getText().isEmpty() ||
                        description.getText().isEmpty() || createdBy.getText().isEmpty() ||
                        accountCode.getText().isEmpty() || amount.getText().isEmpty() ||
                        vatRate.getText().isEmpty() || totalAmount.getText().isEmpty() ||
                        receiptDate.getValue() == null || receiptType.getValue() == null ||
                        paymentType.getValue() == null) {

                    showAlert("Hata", "Lütfen tüm alanları doldurun!", Alert.AlertType.ERROR);
                    return;
                }

                // **Sayısal Değer Kontrolü**
                double enteredAmount = Double.parseDouble(amount.getText());
                double enteredVatRate = Double.parseDouble(vatRate.getText());
                double enteredTotalAmount = Double.parseDouble(totalAmount.getText());

                if (enteredAmount <= 0 || enteredVatRate < 0 || enteredTotalAmount <= 0) {
                    showAlert("Hata", "Tutar, KDV oranı ve toplam tutar pozitif olmalıdır!", Alert.AlertType.ERROR);
                    return;
                }

                // **Tarih Kontrolü**
                if (receiptDate.getValue().isAfter(LocalDate.now())) {
                    showAlert("Hata", "Fiş tarihi bugünden ileri bir tarih olamaz!", Alert.AlertType.ERROR);
                    return;
                }

                // **Kayıt İşlemi**
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
                        enteredAmount,
                        enteredVatRate,
                        enteredTotalAmount,
                        paymentType.getValue()
                );

                boolean isAdded = receiptsDAO.addReceipt(receipt);
                if (isAdded) {
                    showAlert("Başarılı", "Fiş başarıyla kaydedildi!", Alert.AlertType.INFORMATION);
                    refreshReceiptsTable();
                } else {
                    showAlert("Hata", "Fiş eklenirken hata oluştu.", Alert.AlertType.ERROR);
                }

            } catch (NumberFormatException e) {
                showAlert("Hata", "Lütfen geçerli bir sayı girin!", Alert.AlertType.ERROR);
            }
        });
    }

    /**
     * **KDV Dahil Tutarı Otomatik Hesaplama Metodu**
     */
    private void updateTotalAmount(TextField amount, TextField vatRate, TextField totalAmount) {
        try {
            double amt = Double.parseDouble(amount.getText());
            double vat = Double.parseDouble(vatRate.getText());

            if (amt > 0 && vat >= 0) {
                double calculatedTotal = amt + (amt * vat / 100);
                totalAmount.setText(String.format("%.2f", calculatedTotal)); // **Hesaplanan değeri göster**
            } else {
                totalAmount.setText("");
            }
        } catch (NumberFormatException e) {
            totalAmount.setText(""); // **Geçersiz değer girildiyse temizle**
        }
    }


    @FXML
    private void updateReceipt() {
        ReceiptsDTO selectedReceipt = receiptTable.getSelectionModel().getSelectedItem();
        if (selectedReceipt != null) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Fiş Güncelle");
            dialog.setHeaderText("Fiş bilgilerini güncelle:");

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField receiptNumber = new TextField(selectedReceipt.getReceiptNumber());
            receiptNumber.setPromptText("Fiş Numarası");

            DatePicker receiptDate = new DatePicker(selectedReceipt.getReceiptDate());

            TextField taxNumber = new TextField(selectedReceipt.getTaxNumber());
            taxNumber.setPromptText("Vergi Numarası");

            TextField companyName = new TextField(selectedReceipt.getCompanyName());
            companyName.setPromptText("Firma Adı");

            TextField customerName = new TextField(selectedReceipt.getCustomerName());
            customerName.setPromptText("Müşteri Adı");

            TextField description = new TextField(selectedReceipt.getDescription());
            description.setPromptText("Açıklama");

            TextField createdBy = new TextField(selectedReceipt.getCreatedBy());
            createdBy.setPromptText("Düzenleyen Kişi");

            TextField accountCode = new TextField(selectedReceipt.getAccountCode());
            accountCode.setPromptText("Hesap Kodu");

            ComboBox<String> receiptType = new ComboBox<>();
            receiptType.getItems().addAll("Ödeme", "Tahsilat", "Masraf", "Gelir");
            receiptType.setValue(selectedReceipt.getReceiptType());

            TextField amount = new TextField(String.valueOf(selectedReceipt.getAmount()));
            amount.setPromptText("Tutar");

            TextField vatRate = new TextField(String.valueOf(selectedReceipt.getVatRate()));
            vatRate.setPromptText("KDV Oranı");

            TextField totalAmount = new TextField(String.valueOf(selectedReceipt.getTotalAmount()));
            totalAmount.setPromptText("KDV Dahil Tutar");

            ComboBox<String> paymentType = new ComboBox<>();
            paymentType.getItems().addAll("Nakit", "Kredi Kartı", "Havale", "Çek");
            paymentType.setValue(selectedReceipt.getPaymentType());

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
                try {
                    if (receiptNumber.getText().isEmpty() || companyName.getText().isEmpty() ||
                            customerName.getText().isEmpty() || amount.getText().isEmpty() ||
                            vatRate.getText().isEmpty() || totalAmount.getText().isEmpty()) {

                        showAlert("Hata", "Lütfen tüm alanları doldurun!", Alert.AlertType.ERROR);
                        return;
                    }

                    double updatedAmount = Double.parseDouble(amount.getText());
                    double updatedVatRate = Double.parseDouble(vatRate.getText());
                    double updatedTotalAmount = Double.parseDouble(totalAmount.getText());

                    ReceiptsDTO updatedReceipt = new ReceiptsDTO(
                            selectedReceipt.getId(),
                            receiptNumber.getText(),
                            receiptDate.getValue(),
                            taxNumber.getText(),
                            companyName.getText(),
                            customerName.getText(),
                            description.getText(),
                            createdBy.getText(),
                            accountCode.getText(),
                            receiptType.getValue(),
                            updatedAmount,
                            updatedVatRate,
                            updatedTotalAmount,
                            paymentType.getValue()
                    );

                    boolean isUpdated = receiptsDAO.updateReceipt(updatedReceipt);
                    if (isUpdated) {
                        showAlert("Başarılı", "Fiş başarıyla güncellendi!", Alert.AlertType.INFORMATION);
                        refreshReceiptsTable();
                    } else {
                        showAlert("Hata", "Fiş güncellenirken hata oluştu.", Alert.AlertType.ERROR);
                    }

                } catch (NumberFormatException e) {
                    showAlert("Hata", "Lütfen geçerli sayısal değerler girin!", Alert.AlertType.ERROR);
                }
            });
        } else {
            showAlert("Hata", "Lütfen güncellenecek bir fiş seçin!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void deleteReceipt() {
        ReceiptsDTO selectedReceipt = receiptTable.getSelectionModel().getSelectedItem();
        if (selectedReceipt != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Silme Onayı");
            alert.setHeaderText("Seçili fişi silmek istediğinizden emin misiniz?");
            alert.setContentText("Bu işlem geri alınamaz!");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                receiptsDAO.deleteReceipt(selectedReceipt.getId());
                refreshReceiptsTable();
                showAlert("Başarılı", "Fiş başarıyla silindi!", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Hata", "Lütfen silinecek bir fiş seçin!", Alert.AlertType.ERROR);
        }
    }

    private ReceiptsDTO showReceiptDialog(ReceiptsDTO receipt) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(receipt == null ? "Yeni Fiş Ekle" : "Fiş Güncelle");
        dialog.setHeaderText("Fiş bilgilerini girin:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField receiptNumber = new TextField(receipt != null ? receipt.getReceiptNumber() : "");
        receiptNumber.setPromptText("Fiş Numarası");
        DatePicker receiptDate = new DatePicker(receipt != null ? receipt.getReceiptDate() : LocalDate.now());
        TextField taxNumber = new TextField(receipt != null ? receipt.getTaxNumber() : "");
        TextField companyName = new TextField(receipt != null ? receipt.getCompanyName() : "");
        TextField customerName = new TextField(receipt != null ? receipt.getCustomerName() : "");
        TextField description = new TextField(receipt != null ? receipt.getDescription() : "");
        TextField createdBy = new TextField(receipt != null ? receipt.getCreatedBy() : "");
        TextField accountCode = new TextField(receipt != null ? receipt.getAccountCode() : "");
        TextField receiptType = new TextField(receipt != null ? receipt.getReceiptType() : "");
        TextField amount = new TextField(receipt != null ? String.valueOf(receipt.getAmount()) : "");
        TextField vatRate = new TextField(receipt != null ? String.valueOf(receipt.getVatRate()) : "");
        TextField totalAmount = new TextField(receipt != null ? String.valueOf(receipt.getTotalAmount()) : "");
        TextField paymentType = new TextField(receipt != null ? receipt.getPaymentType() : "");

        grid.add(new Label("Fiş Numarası:"), 0, 0);
        grid.add(receiptNumber, 1, 0);
        grid.add(new Label("Tarih:"), 0, 1);
        grid.add(receiptDate, 1, 1);
        grid.add(new Label("Vergi No:"), 0, 2);
        grid.add(taxNumber, 1, 2);
        // Diğer alanları ekleyin...

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait();
        if (dialog.getResult() == ButtonType.OK) {
            return new ReceiptsDTO(0, receiptNumber.getText(), receiptDate.getValue(), taxNumber.getText(),
                    companyName.getText(), customerName.getText(), description.getText(), createdBy.getText(),
                    accountCode.getText(), receiptType.getText(), Double.parseDouble(amount.getText()),
                    Double.parseDouble(vatRate.getText()), Double.parseDouble(totalAmount.getText()), paymentType.getText());
        }
        return null;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /// ////////////////////////////////////////////////////////////////////////////////////////////////

    @FXML
    private void promptEmailAndSend() {
        // Kullanıcıdan e-posta adresini isteme
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("E-Posta Gönder");
        dialog.setHeaderText("Fişleri göndermek için e-posta adresinizi girin:");
        dialog.setContentText("E-Posta Adresi:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(email -> {
            if (!isValidEmail(email)) {
                showAlert("Hata", "Geçerli bir e-posta adresi girin!", Alert.AlertType.ERROR);
            } else {
                File file = createExcelFile();
                if (file != null) {
                    sendEmailWithAttachment(email, "Fişleriniz", "Ek olarak fiş bilgileri Excel dosyası eklenmiştir.", file);
                }
            }
        });
    }
    @FXML
    private File createExcelFile() {
        List<ReceiptsDTO> receipts = receiptsDAO.getAllReceipts();

        if (receipts.isEmpty()) {
            showAlert("Bilgi", "Aktarılacak fiş bulunmamaktadır!", Alert.AlertType.INFORMATION);
            return null;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Receipts");

        Row headerRow = sheet.createRow(0);
        String[] columns = {"Fiş No", "Tarih", "Vergi No", "Firma Adı", "Müşteri Adı", "Açıklama", "Düzenleyen", "Hesap Kodu", "Fiş Türü", "Tutar", "KDV Oranı", "Toplam Tutar", "Ödeme Türü"};

        for (int i = 0; i < columns.length; i++) {
            org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        int rowNum = 1;
        for (ReceiptsDTO receipt : receipts) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(receipt.getReceiptNumber());
            row.createCell(1).setCellValue(receipt.getReceiptDate().toString());
            row.createCell(2).setCellValue(receipt.getTaxNumber());
            row.createCell(3).setCellValue(receipt.getCompanyName());
            row.createCell(4).setCellValue(receipt.getCustomerName());
            row.createCell(5).setCellValue(receipt.getDescription());
            row.createCell(6).setCellValue(receipt.getCreatedBy());
            row.createCell(7).setCellValue(receipt.getAccountCode());
            row.createCell(8).setCellValue(receipt.getReceiptType());
            row.createCell(9).setCellValue(receipt.getAmount());
            row.createCell(10).setCellValue(receipt.getVatRate());
            row.createCell(11).setCellValue(receipt.getTotalAmount());
            row.createCell(12).setCellValue(receipt.getPaymentType());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        File file = new File("Receipts.xlsx");

        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
            workbook.close();
            showAlert("Başarılı", "Fişler başarıyla Excel dosyasına aktarıldı!", Alert.AlertType.INFORMATION);
            return file;
        } catch (IOException e) {
            showAlert("Hata", "Excel dosyası oluşturulurken hata oluştu!", Alert.AlertType.ERROR);
            e.printStackTrace();
            return null;
        }
    }

    private void sendEmailWithAttachment(String recipient, String subject, String messageBody, File attachment) {
        final String senderEmail = "denememalatya4444@gmail.com";
        final String senderPassword = "xcgz husy hkut diip";  // **Güvenlik için çevresel değişken veya güvenli depolama kullan!**

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            // 📌 E-posta İçeriği ve Ekler
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(messageBody);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(attachment);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);
            showAlert("Başarılı", "E-posta başarıyla gönderildi!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Hata", "E-posta gönderme başarısız!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }


/// ///////////////////////////////////////
    // Yazıcı

    @FXML
    private void printExcelFile() {
        File file = new File("Receipts.xlsx");
        if (!file.exists()) {
            showAlert("Hata", "Önce Excel dosyası oluşturun!", Alert.AlertType.ERROR);
            return;
        }

        try {
            // **Excel dosyasını PDF olarak yazdırmak için Apache POI ve Print API kullanımı**
            InputStream inputStream = new FileInputStream(file);
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();

            if (printService == null) {
                showAlert("Hata", "Yazıcı bulunamadı!", Alert.AlertType.ERROR);
                return;
            }

            DocPrintJob printJob = printService.createPrintJob();
            Doc document = new SimpleDoc(inputStream, flavor, null);
            printJob.print(document, attributes);

            inputStream.close();
            showAlert("Başarılı", "Excel dosyası başarıyla yazdırıldı!", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            showAlert("Hata", "Yazdırma işlemi sırasında hata oluştu!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /// ////////////////////////////////////////////////////////////////
    // Hesap Makinesi

        @FXML
        private void openCalculator() {
            String os = System.getProperty("os.name").toLowerCase();

            try {
                if (os.contains("win")) {
                    // Windows için hesap makinesini aç
                    Runtime.getRuntime().exec("calc");
                } else if (os.contains("mac")) {
                    // MacOS için hesap makinesini aç
                    Runtime.getRuntime().exec("open -a Calculator");
                } else if (os.contains("nix") || os.contains("nux") || os.contains("linux")) {
                    // Linux için hesap makinesini aç
                    Runtime.getRuntime().exec("gnome-calculator");
                } else {
                    showAlert("Hata", "Bu işletim sistemi desteklenmiyor!", Alert.AlertType.ERROR);
                }
            } catch (IOException e) {
                showAlert("Hata", "Hesap makinesi açılamadı!", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }

}
