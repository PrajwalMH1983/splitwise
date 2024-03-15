package com.splitwise.money.service;

import com.splitwise.money.entity.Expense;
import com.splitwise.money.entity.Group;
import com.splitwise.money.entity.User;

public interface ExpenseService {
    Expense addExpense(Expense Expense , Group groupId , User userId);

    String deleteExpense(Long expenseId);

    Expense splitExpenseEqual(Expense expense);

    void save(Expense expense);

}
