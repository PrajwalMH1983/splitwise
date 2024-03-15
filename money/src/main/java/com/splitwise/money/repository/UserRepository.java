package com.splitwise.money.repository;

import com.splitwise.money.entity.Group;
import com.splitwise.money.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUserName(String userName);
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);

    Set<Group> findGroupsByUserId(Long userId);

}
