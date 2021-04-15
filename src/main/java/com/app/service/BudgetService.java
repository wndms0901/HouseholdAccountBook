package com.app.service;

import com.app.dto.BudgetDto;
import com.app.dto.BudgetExpenditureDto;
import com.app.dto.BudgetRequestDto;

import java.util.List;

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

    /**
     * 예산 대비 지출 목록 조회
     * @param budgetRequestDto
     * @return List<BudgetExpenditureDto>
     */
    List<BudgetExpenditureDto> selectBudgetExpenditureList(BudgetRequestDto budgetRequestDto);
}
