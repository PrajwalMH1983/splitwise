package com.splitwise.money.repository;

import com.splitwise.money.entity.Group;
import com.splitwise.money.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Set<User> findUsersByGroupId(Long groupId);
}
