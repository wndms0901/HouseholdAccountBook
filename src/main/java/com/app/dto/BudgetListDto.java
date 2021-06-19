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
    private String budgetAmount;
    private String budgetDate;
    private Long expenditureAmount;
    private Long total;

    public Budget saveExpenditureBudget(UserDto userDto){
        return Budget.builder()
                .budgetAmount(Long.parseLong(budgetAmount))
                .budgetDate(budgetDate)
                .largeCategoryId(largeCategoryId)
                .user(userDto.toEntity())
                .build();
    }
}
