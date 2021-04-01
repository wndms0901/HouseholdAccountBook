package com.app.repository;

import com.app.domain.ExpenditureBudget;
import com.app.domain.IncomeBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpenditureBudgetRepository extends JpaRepository<ExpenditureBudget, Long> {
    Optional<ExpenditureBudget> findByUserEmailAndExpenditureBudgetDateAndLargeCategoryId(String email, String expenditureBudgetDate, Long largeCategoryId);
}
