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
    private String incomeBudgetAmount;
    private List<BudgetListDto> budgetListDtoList;

    @Builder
    public BudgetDto(String incomeBudgetAmount, List<BudgetListDto> budgetListDtoList) {
        this.incomeBudgetAmount = incomeBudgetAmount;
        this.budgetListDtoList = budgetListDtoList;
    }
}
