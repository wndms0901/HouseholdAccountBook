package com.app.repository;

import com.app.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserEmailAndBudgetDateAndLargeCategoryId(String email, String BudgetDate, Long largeCategoryId);
}
