package com.splitwise.money.service;

import com.splitwise.money.entity.Group;
import com.splitwise.money.entity.Transaction;
import com.splitwise.money.entity.User;

public interface TransactionService {
    String makeTransaction(User payer , User receiver , Transaction transaction);
}
