package com.splitwise.money.entity;

import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

public class TransactionListener {
    @PrePersist
    public void setPaidAt(Transaction transaction){
        transaction.setPaidAt(LocalDateTime.now());
    }
}
