package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class BudgetDto {
    private Long expenditurebudgetId;
    private int expenditureBudgetAmount;
    private String expenditureBudgetDate;
    private Long largeCategoryId;
    private Long incomeBudgetId;
    private int incomeBudgetAmount;
    private String incomeBudgetDate;
    private UserDto userDto;
}
