package com.splitwise.money.service;

import com.splitwise.money.entity.User;
import com.splitwise.money.exception.AlreadyExistsException;

import java.util.Optional;

public interface UserService {

    User updateUserAccount(Long userId , User user);

    Boolean isUserNameAvailable(String username);

    Boolean isEmailAvailable(String email);
    void save(User user);

    Boolean userExistsById(Long userId);

    Optional<User> findUserById(Long userId);

    Boolean existsUserByUserName(String userName);

    Boolean existsByEmail(String email);


}
