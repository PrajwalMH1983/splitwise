package com.splitwise.money.service.serviceImpl;

import com.splitwise.money.entity.CompositeKey;
import com.splitwise.money.entity.Expense;
import com.splitwise.money.entity.ExpenseParticipants;
import com.splitwise.money.entity.User;
import com.splitwise.money.repository.ExpenseParticipantsRepository;
import com.splitwise.money.repository.ExpenseRepository;
import com.splitwise.money.repository.UserRepository;
import com.splitwise.money.service.ExpenseParticipantsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class ExpenseParticipantsServiceImpl implements ExpenseParticipantsService {

    @Autowired
    private ExpenseParticipantsRepository expenseParticipantsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public void addBalance(User payer, User participant, Double newExpenseAmount, Expense newExpense) {
        try {
            Optional<ExpenseParticipants> existingRowOptional = expenseParticipantsRepository.
                    findByPayerAndParticipant(payer, participant);
            Optional<ExpenseParticipants> existingDifferentRowOptional = expenseParticipantsRepository.
                    findByPayerAndParticipant(participant, payer);
            if (existingRowOptional.isPresent()) {
                ExpenseParticipants existingRow = existingRowOptional.get();
                existingRow.setAmountOwed(existingRow.getAmountOwed() + newExpenseAmount);
                expenseParticipantsRepository.save(existingRow);
            } else {
                if (existingDifferentRowOptional.isPresent()) {
                    ExpenseParticipants existingRow = existingDifferentRowOptional.get();
                    existingRow.setAmountOwed(existingRow.getAmountOwed() - newExpenseAmount);
                    expenseParticipantsRepository.save(existingRow);
                    return;
                }
                ExpenseParticipants newExpenseParticipant = new ExpenseParticipants();
                CompositeKey compositeKey = new CompositeKey();
                Random random = new Random();
                compositeKey.setEid(random.nextLong());
                compositeKey.setExpense_id(newExpense.getExpenseId());
                newExpenseParticipant.setId(compositeKey);
                newExpenseParticipant.setExpense(newExpense);
                newExpenseParticipant.setPayer(payer);
                newExpenseParticipant.setParticipant(participant);
                newExpenseParticipant.setAmountOwed(newExpenseAmount);
                if (newExpenseParticipant.getId().getExpense_id() != null)
                    expenseParticipantsRepository.save(newExpenseParticipant);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.warn("[MethodError] : @addBalance : {}" , e.getMessage());
        }
    }

    @Override
    public String getBalance(User payer, User receiver, Double amountPaying) {
        try {
            Optional<ExpenseParticipants> payerAndReceiver = expenseParticipantsRepository.
                    findByPayerAndParticipant(payer, receiver);
            Optional<ExpenseParticipants> receiverAndPayer = expenseParticipantsRepository.
                    findByPayerAndParticipant(receiver, payer);

            ExpenseParticipants settling = null;
            if (payerAndReceiver.isPresent()) {
                settling = payerAndReceiver.get();
                settling.setAmountOwed(settling.getAmountOwed() + amountPaying);
            }
            if (receiverAndPayer.isPresent()) {
                settling = receiverAndPayer.get();
                settling.setAmountOwed(settling.getAmountOwed() - amountPaying);
            }
            if (settling != null) {
                expenseParticipantsRepository.save(settling);
            }

            if (settling != null && settling.getAmountOwed() > 0) {
                return receiver.getUserName() + " Needs to pay " + payer.getUserName() + " " + settling.getAmountOwed() + " amount";
            } else if (settling != null && settling.getAmountOwed() < 0) {
                return payer.getUserName() + " Needs to pay " + receiver.getUserName() + " " + settling.getAmountOwed() * (-1) + " amount";
            } else {
                return "Hurray Settled !";
            }
        }catch (Exception e){
            e.printStackTrace();
            log.warn("[MethodError] : @getBalance : {}" , e.getMessage());
            return "Something Unexpected happened !!";
        }
    }

    @Override
    public Map<Long, Double> findAllBalance(User user) {
        Map<Long, Double> result = new HashMap<>();
        try {
            Optional<List<ExpenseParticipants>> participantSide = expenseParticipantsRepository.findByParticipant(user);
            Optional<List<ExpenseParticipants>> payerSide = expenseParticipantsRepository.findByPayer(user);
            if (participantSide.isPresent()) {
                List<ExpenseParticipants> participants = participantSide.get();
                for (ExpenseParticipants expenseParticipants : participants) {
                    if (!result.containsKey(expenseParticipants.getPayer().getUserId())) {
                        result.put(expenseParticipants.getPayer().getUserId(), (-1) * expenseParticipants.getAmountOwed());
                    }
                }
            }
            if (payerSide.isPresent()) {
                List<ExpenseParticipants> participants = payerSide.get();
                for (ExpenseParticipants expenseParticipants : participants) {
                    if (!result.containsKey(expenseParticipants.getParticipant().getUserId())) {
                        result.put(expenseParticipants.getParticipant().getUserId(), expenseParticipants.getAmountOwed());
                    } else {
                        Double amount = result.get(expenseParticipants.getParticipant().getUserId());
                        amount += expenseParticipants.getAmountOwed();
                        result.put(expenseParticipants.getParticipant().getUserId(), amount);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.warn("[MethodError] : @findAllBalance : {}" , e.getMessage());
        }
        return result;
    }
}
