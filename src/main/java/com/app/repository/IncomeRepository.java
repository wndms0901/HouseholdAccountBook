package com.app.repository;

import com.app.domain.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    @Transactional
    @Modifying
    @Query("delete from Income i where i.incomeId in :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}
