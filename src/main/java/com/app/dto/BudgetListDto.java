package com.app.dto;

import com.app.domain.ExpenditureBudget;
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
    private String expenditureBudgetAmount;
    private String expenditureBudgetDate;
    private int expenditureAmount;
    private int total;

    public ExpenditureBudget saveExpenditureBudget(UserDto userDto){
        return ExpenditureBudget.builder()
                .expenditureBudgetAmount(Integer.parseInt(expenditureBudgetAmount))
                .expenditureBudgetDate(expenditureBudgetDate)
                .largeCategoryId(largeCategoryId)
                .user(userDto.toEntity())
                .build();
    }
}
