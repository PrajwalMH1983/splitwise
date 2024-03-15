package com.splitwise.money.service;

import com.splitwise.money.entity.Group;
import com.splitwise.money.entity.User;
import com.splitwise.money.exception.AlreadyExistsException;

import java.util.Map;
import java.util.Optional;

public interface GroupService {

    Group addUserToGroup(Group group ,Long userId);
    String removeUserFromGroup(Long userId , Group group);

    String deleteGroup(Long groupId , User user);

    void save(Group group);

    Boolean groupExistsById(Long groupId);

    Optional<Group> findGroupById(Long groupId);

}
