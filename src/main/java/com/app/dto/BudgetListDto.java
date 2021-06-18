package com.app.dto;

import com.app.domain.Budget;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BudgetListDto {
    private Long largeCategoryId;
    private String largeCategoryName;
    //private Long expenditurebudgetId;
    private String budgetAmount;
    private String budgetDate;
    private int expenditureAmount;
    private int total;

    public Budget saveExpenditureBudget(UserDto userDto){
        return Budget.builder()
                .budgetAmount(Long.parseLong(budgetAmount))
                .budgetDate(budgetDate)
                .largeCategoryId(largeCategoryId)
                .user(userDto.toEntity())
                .build();
    }
}
