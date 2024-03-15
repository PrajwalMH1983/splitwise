package com.splitwise.money.service.serviceImpl;

import com.splitwise.money.entity.User;
import com.splitwise.money.repository.UserRepository;
import com.splitwise.money.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User updateUserAccount(Long userId, User user) {
        try {
            Optional<User> user1 = userRepository.findById(userId);
            if (user1.isPresent()) {
                User existingUser = user1.get();
                existingUser.setPhoneNumber(user.getPhoneNumber());
                existingUser.setEmail(user.getEmail());
                existingUser.setUserName(user.getUserName());
                userRepository.save(existingUser);
                return existingUser;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Boolean isUserNameAvailable(String username) {
        return userRepository.findByUserName(username).isEmpty();
    }

    @Override
    public Boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public Boolean userExistsById(Long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Boolean existsUserByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
