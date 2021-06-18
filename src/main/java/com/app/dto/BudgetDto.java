package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BudgetDto {
    private String totalIncome;
    private String threeMonthAverageExpenditure;
    private String lastMonthExpenditure;
    private List<BudgetListDto> budgetListDtoList;
    private String budgetDate;
    private UserDto userDto;

    @Builder
    public BudgetDto(String totalIncome, String threeMonthAverageExpenditure, String lastMonthExpenditure, List<BudgetListDto> budgetListDtoList) {
        this.totalIncome = totalIncome;
        this.threeMonthAverageExpenditure = threeMonthAverageExpenditure;
        this.lastMonthExpenditure = lastMonthExpenditure;
        this.budgetListDtoList = budgetListDtoList;
    }

}
