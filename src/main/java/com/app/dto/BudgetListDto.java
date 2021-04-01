package com.app.dto;

import com.app.domain.ExpenditureBudget;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BudgetListDto {
    private Long expenditurebudgetId;
    private int expenditureBudgetAmount;
    private String expenditureBudgetDate;
    private Long largeCategoryId;
    private String largeCategoryName;
    private int expenditureAmount;

    public ExpenditureBudget saveExpenditureBudget(UserDto userDto){
        return ExpenditureBudget.builder()
                .expenditureBudgetAmount(expenditureBudgetAmount)
                .expenditureBudgetDate(expenditureBudgetDate)
                .largeCategoryId(largeCategoryId)
                .user(userDto.toEntity())
                .build();
    }
}
