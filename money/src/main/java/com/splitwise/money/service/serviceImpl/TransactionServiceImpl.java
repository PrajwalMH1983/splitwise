package com.splitwise.money.service.serviceImpl;

import com.splitwise.money.entity.Group;
import com.splitwise.money.entity.Transaction;
import com.splitwise.money.entity.User;
import com.splitwise.money.repository.GroupRepository;
import com.splitwise.money.repository.TransactionRepository;
import com.splitwise.money.repository.UserRepository;
import com.splitwise.money.service.ExpenseParticipantsService;
import com.splitwise.money.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ExpenseParticipantsService expenseParticipantsService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public String makeTransaction(User payer, User receiver, Transaction transaction) {
        try {
            String response = expenseParticipantsService.getBalance(payer , receiver , transaction.getAmount());
            //group.setTotalExpenditure(group.getTotalExpenditure() + amount);
            System.out.println("Transaction : " + transaction);
            transactionRepository.save(transaction);
            return response;
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
