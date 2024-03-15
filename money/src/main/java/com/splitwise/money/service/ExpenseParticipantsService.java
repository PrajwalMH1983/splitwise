package com.splitwise.money.service;

import com.splitwise.money.entity.Expense;
import com.splitwise.money.entity.ExpenseParticipants;
import com.splitwise.money.entity.User;

import java.util.Map;

public interface ExpenseParticipantsService {

    void addBalance(User payer , User participant , Double expenseAmount , Expense expense);

    String getBalance(User user1 , User user2 , Double amount);

    Map<Long , Double> findAllBalance(User user);
}
