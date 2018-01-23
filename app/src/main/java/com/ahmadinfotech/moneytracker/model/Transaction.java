package com.ahmadinfotech.moneytracker.model;

import java.util.Date;

/**
 * Created by root on 19/1/18.
 */

public class Transaction {
    private String id;
    private String lenderId;
    private Date transactionDate;
    private Date addedDate;
    private Date updatedDate;
    private String description;
    private double amount;
    private String transactionType;
    private String paymentMode;
    private String paymentAccount;
    private String transactionCategory;
    private boolean cashbackEarned;
    private double cashbackAmount;
    private String merchant;
    private String merchantAccount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLenderId() {
        return lenderId;
    }

    public void setLenderId(String lenderId) {
        this.lenderId = lenderId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public boolean isCashbackEarned() {
        return cashbackEarned;
    }

    public void setCashbackEarned(boolean cashbackEarned) {
        this.cashbackEarned = cashbackEarned;
    }

    public double getCashbackAmount() {
        return cashbackAmount;
    }

    public void setCashbackAmount(double cashbackAmount) {
        this.cashbackAmount = cashbackAmount;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getMerchantAccount() {
        return merchantAccount;
    }

    public void setMerchantAccount(String merchantAccount) {
        this.merchantAccount = merchantAccount;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }
}
