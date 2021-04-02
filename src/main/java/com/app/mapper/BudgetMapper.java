package com.app.mapper;


import com.app.dto.BudgetDto;
import com.app.dto.BudgetListDto;
import com.app.dto.BudgetRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BudgetMapper {
    /**
     * 수입 예산 금액 조회
     * @param budgetRequestDto
     * @return String
     */
    String selectIncomeBudgetAmount(BudgetRequestDto budgetRequestDto);

    /**
     * 3개월 간 평균 지출 조회
     * @param budgetRequestDto
     * @return String
     */
    String selectThreeMonthAverageExpenditure(BudgetRequestDto budgetRequestDto);

    /**
     * 지난달 지출 조회
     * @param budgetRequestDto
     * @return String
     */
    String selectLastMonthExpenditure(BudgetRequestDto budgetRequestDto);

    /**
     * 카테고리별 예산 목록 조회
     * @param budgetRequestDto
     * @return List<BudgetListDto>
     */
    List<BudgetListDto> selectBudgetList(BudgetRequestDto budgetRequestDto);

}