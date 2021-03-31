package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BudgetListDto {
    private Long largeCategoryId;
    private String largeCategoryName;
    private Long expenditurebudgetId;
    private int expenditureBudgetAmount;
    private String expenditureBudgetDate;
    private int expenditureAmount;
    private UserDto userDto;
}
