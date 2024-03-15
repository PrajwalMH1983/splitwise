package com.splitwise.money.Utils;

import com.splitwise.money.entity.Expense;
import com.splitwise.money.entity.Group;
import com.splitwise.money.entity.User;
import com.splitwise.money.payload.request.CreateExpenseRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BasicUtils {

    public static void checkUserInGroup(CreateExpenseRequest createExpenseRequest , Group group , Expense expense){
        String[] users = createExpenseRequest.getParticipants().split(",");
        Map<Long , User> userIds = new HashMap<>();
        for(User user : group.getUsersInGroup()){
            userIds.put(user.getUserId() , user);
        }
        for(String userId : users){
            if(userIds.containsKey(Long.valueOf(userId))) {
                expense.getParticipants().add(userIds.get(Long.valueOf(userId)));
            }
        }
    }
}
