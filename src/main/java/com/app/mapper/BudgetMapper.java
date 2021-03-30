package com.app.mapper;


import com.app.dto.BudgetDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BudgetMapper {
    /**
     * 수입 예산 조회
     * @param budgetDto
     * @return int
     */
    public int selectIncomeBudgetAmount(BudgetDto budgetDto);
}
