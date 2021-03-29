package com.app.repository;

import com.app.domain.AccountCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountCategoryRepository extends JpaRepository<AccountCategory, Long> {
    List<AccountCategory> findAllByAccountCategoryType(String accountCategoryType);
}
