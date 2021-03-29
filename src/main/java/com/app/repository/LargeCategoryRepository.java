package com.app.repository;

import com.app.domain.LargeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LargeCategoryRepository extends JpaRepository<LargeCategory, Long> {
    List<LargeCategory> findAllByCategoryType(String categoryType);
}
