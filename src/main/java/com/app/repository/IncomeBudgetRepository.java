package com.app.repository;

import com.app.domain.IncomeBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncomeBudgetRepository extends JpaRepository<IncomeBudget, Long> {
    Optional<IncomeBudget> findByUserEmailAndIncomeBudgetDate(String email, String incomeBudgetDate);
}
