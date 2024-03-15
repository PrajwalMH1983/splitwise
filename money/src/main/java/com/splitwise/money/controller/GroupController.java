package com.splitwise.money.controller;

import com.splitwise.money.Utils.BasicUtils;
import com.splitwise.money.entity.Expense;
import com.splitwise.money.entity.Group;
import com.splitwise.money.entity.User;
import com.splitwise.money.payload.request.CreateExpenseRequest;
import com.splitwise.money.payload.request.CreateGroupRequest;
import com.splitwise.money.payload.request.SignUpRequest;
import com.splitwise.money.payload.response.MessageResponse;
import com.splitwise.money.repository.GroupRepository;
import com.splitwise.money.repository.UserRepository;
import com.splitwise.money.service.ExpenseService;
import com.splitwise.money.service.GroupService;
import com.splitwise.money.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ExpenseService expenseService;


    @PostMapping("/create/{userId}")
    public ResponseEntity<?> createGroup(@Valid @RequestBody CreateGroupRequest createGroupRequest
            , @PathVariable Long userId) {


        if (!userService.userExistsById(userId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error : User Does Not Exist  !"));
        }

        Optional<User> userOptional = userService.findUserById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Group group = new Group();
            group.setTitle(createGroupRequest.getTitle());
            group.setDescription(createGroupRequest.getDescription());
            group.getUsersInGroup().add(user);
            groupService.save(group);
            return ResponseEntity.status(201).body(new MessageResponse("Group created Successfully"));
        }


        return ResponseEntity.status(500).body(new MessageResponse("Group Not created Successfully"));
    }

    @GetMapping("/addUser")
    public ResponseEntity<?> addUsersToGroup(@RequestParam Long userId, @RequestParam Long groupId) {
        try {

            if (!groupService.groupExistsById(groupId)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error : Group Does Not exist !"));
            }

            if (!userService.userExistsById(userId)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error : User Does Not exist !"));
            }

            Optional<Group> groupOptional = groupService.findGroupById(groupId);
            if (groupOptional.isPresent()) {
                groupService.addUserToGroup(groupOptional.get(), userId);
                return ResponseEntity.status(200).body(new MessageResponse("User Added in Group Successfully"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(500).body(new MessageResponse("Error while Adding User "));
    }

    @PostMapping("/expense")
    public ResponseEntity<?> addExpenseToGroup(@Valid @RequestBody CreateExpenseRequest createExpenseRequest
            , @RequestParam Long paidBy, @RequestParam Long groupId) {
        try {

            if (!groupService.groupExistsById(groupId)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error : Group Does Not exist !"));
            }

            if (!userService.userExistsById(paidBy)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error : User Does Not exist !"));
            }
            Optional<Group> groupOptional = groupService.findGroupById(groupId);
            Optional<User> userOptional = userService.findUserById(paidBy);
            if (groupOptional.isPresent() && userOptional.isPresent() &&
                    groupOptional.get().getUsersInGroup().contains(userOptional.get())) {
                Expense expense = new Expense();
                expense.setPaidBy(userOptional.get());
                expense.setDescription(createExpenseRequest.getDescription());
                expense.setInvolvedGroup(groupOptional.get());
                expense.setTotalAmount(createExpenseRequest.getTotalAmount());
                expenseService.save(expense);
                if (createExpenseRequest.getParticipants() != null &&
                        !createExpenseRequest.getParticipants().isBlank()) {
                    BasicUtils.checkUserInGroup(createExpenseRequest, groupOptional.get(), expense);
                } else {
                    expense.getParticipants().addAll(groupOptional.get().getUsersInGroup());
                }
                expenseService.addExpense(expense, groupOptional.get(), userOptional.get());
                return ResponseEntity.status(200).body(new MessageResponse("Expense Added in Group Successfully"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(500).body(new MessageResponse("Error while Adding Expense "));
    }

}
