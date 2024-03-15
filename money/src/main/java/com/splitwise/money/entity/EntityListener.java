package com.splitwise.money.entity;

import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

public class EntityListener {
    @PrePersist
    public void setCreatedAt(Expense expense){
        expense.setCreatedAt(LocalDateTime.now());
    }
}
