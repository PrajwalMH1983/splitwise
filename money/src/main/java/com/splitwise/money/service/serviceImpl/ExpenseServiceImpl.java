package com.splitwise.money.service.serviceImpl;

import com.splitwise.money.entity.*;
import com.splitwise.money.repository.ExpenseParticipantsRepository;
import com.splitwise.money.repository.ExpenseRepository;
import com.splitwise.money.repository.GroupRepository;
import com.splitwise.money.repository.UserRepository;
import com.splitwise.money.service.ExpenseParticipantsService;
import com.splitwise.money.service.ExpenseService;
import com.splitwise.money.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseParticipantsService expenseParticipantsService;

    @Autowired
    private ExpenseParticipantsRepository expenseParticipantsRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public Expense addExpense(Expense expense , Group group , User user) {
        try {
            if (group.getUsersInGroup().contains(user)) {
                expense = splitExpenseEqual(expense);
                group.getExpenses().add(expense);
                groupRepository.save(group);
            }
            expenseRepository.save(expense);
        }catch (Exception e){
            e.printStackTrace();
            log.warn("[MethodError] : @addExpense : {}" , e.getMessage());
        }
        return expense;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public String deleteExpense(Long expenseId) {
        try{
            Optional<Expense> expense = expenseRepository.findById(expenseId);
            if (!expense.isPresent()) {
                return "Could Not Find the expense!!";
            }
            expenseRepository.delete(expense.get());
        } catch (Exception e){
            e.printStackTrace();
            log.warn("[MethodError] : @deleteExpense : {}" , e.getMessage());
        }
        return "Successfully Deleted the Expense !!";
    }

    @Override
    public Expense splitExpenseEqual(Expense expense) {
        try {
            Double totalAmount = expense.getTotalAmount();
            Integer totalUsersInGroup = expense.getParticipants().size();
            Double exactSplit = (totalAmount / totalUsersInGroup);
            expense.getParticipants().forEach(participant -> {
                if (!expense.getPaidBy().equals(participant)) {
                    expenseParticipantsService.addBalance(expense.getPaidBy(), participant, exactSplit, expense);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            log.warn("[MethodError] : @splitExpenseEqual : {}" , e.getMessage());
        }
        return expense;
    }

    @Override
    public void save(Expense expense) {
        expenseRepository.save(expense);
    }
}
