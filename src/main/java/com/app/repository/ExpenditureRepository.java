package com.app.repository;

import com.app.domain.Expenditure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExpenditureRepository extends JpaRepository<Expenditure, Long> {
    @Transactional
    @Modifying
    @Query("delete from Expenditure e where e.expenditureId in :ids")
    void deleteAllByIdInQuery(@Param("ids") List<Long> ids);
}
