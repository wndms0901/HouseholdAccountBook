package com.app.mapper;


import com.app.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BudgetMapper {
    /**
     * 수입 합계 조회
     * @param budgetRequestDto
     * @return String
     */
    String selectTotalIncome(BudgetRequestDto budgetRequestDto);

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

    /**
     * 월별 예산 대비 지출 목록 조회
     * @param periodDto
     * @return List<BudgetListDto>
     */
    List<BudgetListDto> selectMonthBudgetExpenditureList(PeriodDto periodDto);

}