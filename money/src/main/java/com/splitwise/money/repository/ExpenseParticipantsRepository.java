package com.splitwise.money.repository;

import com.splitwise.money.entity.ExpenseParticipants;
import com.splitwise.money.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseParticipantsRepository extends JpaRepository<ExpenseParticipants , Long> {

    Optional<ExpenseParticipants> findByPayerAndParticipant(User payerId , User participantId);

    Optional<List<ExpenseParticipants>> findByParticipant(User user);
    Optional<List<ExpenseParticipants>> findByPayer(User user);
}
