package com.hamitmizrak.vatcalculation.dao;

import com.hamitmizrak.vatcalculation.databases.SingletonDBConnection;
import com.hamitmizrak.vatcalculation.dto.ReceiptsDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReceiptsDAO {
    private Connection connection;

    public ReceiptsDAO() {
        this.connection = SingletonDBConnection.getConnection();
    }

    // Fiş ekleme
    public boolean addReceipt(ReceiptsDTO receipt) {
        String sql = "INSERT INTO receipts (receipt_number, receipt_date, tax_number, company_name, customer_name, description, created_by, account_code, receipt_type, amount, vat_rate, total_amount, payment_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, receipt.getReceiptNumber());
            statement.setDate(2, java.sql.Date.valueOf(receipt.getReceiptDate()));
            statement.setString(3, receipt.getTaxNumber());
            statement.setString(4, receipt.getCompanyName());
            statement.setString(5, receipt.getCustomerName());
            statement.setString(6, receipt.getDescription());
            statement.setString(7, receipt.getCreatedBy());
            statement.setString(8, receipt.getAccountCode());
            statement.setString(9, receipt.getReceiptType());
            statement.setDouble(10, receipt.getAmount());
            statement.setDouble(11, receipt.getVatRate());
            statement.setDouble(12, receipt.getTotalAmount());
            statement.setString(13, receipt.getPaymentType());
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tüm fişleri listeleme
    public List<ReceiptsDTO> getAllReceipts() {
        List<ReceiptsDTO> receipts = new ArrayList<>();
        String sql = "SELECT * FROM receipts";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                receipts.add(new ReceiptsDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("receipt_number"),
                        resultSet.getDate("receipt_date").toLocalDate(),
                        resultSet.getString("tax_number"),
                        resultSet.getString("company_name"),
                        resultSet.getString("customer_name"),
                        resultSet.getString("description"),
                        resultSet.getString("created_by"),
                        resultSet.getString("account_code"),
                        resultSet.getString("receipt_type"),
                        resultSet.getDouble("amount"),
                        resultSet.getDouble("vat_rate"),
                        resultSet.getDouble("total_amount"),
                        resultSet.getString("payment_type")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receipts;
    }

    // Fiş güncelleme
    public boolean updateReceipt(ReceiptsDTO receipt) {
        String sql = "UPDATE receipts SET receipt_number = ?, receipt_date = ?, tax_number = ?, company_name = ?, customer_name = ?, description = ?, created_by = ?, account_code = ?, receipt_type = ?, amount = ?, vat_rate = ?, total_amount = ?, payment_type = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, receipt.getReceiptNumber());
            statement.setDate(2, java.sql.Date.valueOf(receipt.getReceiptDate()));
            statement.setString(3, receipt.getTaxNumber());
            statement.setString(4, receipt.getCompanyName());
            statement.setString(5, receipt.getCustomerName());
            statement.setString(6, receipt.getDescription());
            statement.setString(7, receipt.getCreatedBy());
            statement.setString(8, receipt.getAccountCode());
            statement.setString(9, receipt.getReceiptType());
            statement.setDouble(10, receipt.getAmount());
            statement.setDouble(11, receipt.getVatRate());
            statement.setDouble(12, receipt.getTotalAmount());
            statement.setString(13, receipt.getPaymentType());
            statement.setInt(14, receipt.getId());
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Fiş silme
    public boolean deleteReceipt(int receiptId) {
        String sql = "DELETE FROM receipts WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, receiptId);
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
