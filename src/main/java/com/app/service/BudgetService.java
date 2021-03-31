package com.app.service;

import com.app.dto.BudgetDto;
import com.app.dto.BudgetRequestDto;

public interface BudgetService {
    /**
     * 예산 목록 조회
     * @param budgetRequestDto
     * @return BudgetDto
     */
    BudgetDto selectBudgetList(BudgetRequestDto budgetRequestDto);

    /**
     * 예산 목록 저장
     * @param budgetDto
     * @return void
     */
    void saveBudgetList(BudgetDto budgetDto);
}
