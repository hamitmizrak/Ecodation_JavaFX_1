package com.hamitmizrak.vatcalculation.dto;


import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReceiptsDTO {
    private int id;
    private String receiptNumber;
    private LocalDate receiptDate;
    private String taxNumber;
    private String companyName;
    private String customerName;
    private String description;
    private String createdBy;
    private String accountCode;
    private String receiptType;
    private double amount;
    private double vatRate;
    private double totalAmount;
    private String paymentType;

    public ReceiptsDTO() {
    }

    public ReceiptsDTO(int id, String receiptNumber, LocalDate receiptDate, String taxNumber, String companyName, String customerName, String description, String createdBy, String accountCode, String receiptType, double amount, double vatRate, double totalAmount, String paymentType) {
        this.id = id;
        this.receiptNumber = receiptNumber;
        this.receiptDate = receiptDate;
        this.taxNumber = taxNumber;
        this.companyName = companyName;
        this.customerName = customerName;
        this.description = description;
        this.createdBy = createdBy;
        this.accountCode = accountCode;
        this.receiptType = receiptType;
        this.amount = amount;
        this.vatRate = vatRate;
        this.totalAmount = totalAmount;
        this.paymentType = paymentType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
