package com.splitwise.money.controller;

import com.splitwise.money.Utils.BasicUtils;
import com.splitwise.money.entity.Expense;
import com.splitwise.money.entity.Group;
import com.splitwise.money.entity.Transaction;
import com.splitwise.money.entity.User;
import com.splitwise.money.payload.request.CreateExpenseRequest;
import com.splitwise.money.payload.request.CreateGroupRequest;
import com.splitwise.money.payload.request.SignUpRequest;
import com.splitwise.money.payload.request.TransactionRequest;
import com.splitwise.money.payload.response.MessageResponse;
import com.splitwise.money.repository.ExpenseParticipantsRepository;
import com.splitwise.money.repository.ExpenseRepository;
import com.splitwise.money.repository.GroupRepository;
import com.splitwise.money.repository.UserRepository;
import com.splitwise.money.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ExpenseParticipantsService expenseParticipantsService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        if (userService.existsUserByUserName(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : UserName is already taken !"));
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : Email is already taken !"));
        }

        //User's account
        User user = new User();
        user.setUserName(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setEmail(signUpRequest.getEmail());
        userService.save(user);

        return ResponseEntity.status(201).body(new MessageResponse("User created Successfully"));
    }

    //As we are not writing the feature of login so need to pass the userid


    @PostMapping("/transaction")
    public ResponseEntity<?> settlingUp(@Valid @RequestBody TransactionRequest transactionRequest
            , @RequestParam Long payerId, @RequestParam Long receiverId) {
        if (!userService.userExistsById(payerId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : Payer Does Not exist !"));
        }
        if (!userService.userExistsById(receiverId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : Receiver Does Not exist !"));
        }
        Optional<User> payer = userService.findUserById(payerId);
        Optional<User> receiver = userService.findUserById(receiverId);

        if (payer.isPresent() && receiver.isPresent()) {
            Transaction transaction = new Transaction();
            transaction.setAmount(transactionRequest.getAmount());
            transaction.setDescription(transactionRequest.getDescription());
            transaction.setPayer(payer.get());
            transaction.setReceiver(receiver.get());
            String response = transactionService.makeTransaction(payer.get(), receiver.get(), transaction);
            return ResponseEntity.
                    status(200).
                    body(new MessageResponse(response));

        }
        return ResponseEntity.status(500).body(new MessageResponse("Error while Making Transaction"));


    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<?> findAllBalances(@PathVariable Long userId) {
        if (!userService.userExistsById(userId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : User Does Not exist !"));
        }
        Optional<User> userOptional = userService.findUserById(userId);
        if (userOptional.isPresent()) {
            Map<Long, Double> response = expenseParticipantsService.findAllBalance(userOptional.get());

            return ResponseEntity.status(200).body(response);
        }
        return ResponseEntity.status(500).body(new MessageResponse("Error while Making Transaction"));
    }

}
