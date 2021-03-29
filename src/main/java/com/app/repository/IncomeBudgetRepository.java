package com.app.repository;

import com.app.domain.IncomeBudget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeBudgetRepository extends JpaRepository<IncomeBudget, Long> {
}
