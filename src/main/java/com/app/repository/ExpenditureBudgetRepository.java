package com.app.repository;

import com.app.domain.ExpenditureBudget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenditureBudgetRepository extends JpaRepository<ExpenditureBudget, Long> {
}
