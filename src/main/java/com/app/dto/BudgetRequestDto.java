package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BudgetRequestDto {
    private String email;
    private String categoryType;
    private String expenditureBudgetDate;
    private String incomeBudgetDate;

    @Builder
    public BudgetRequestDto(String email, String categoryType, String expenditureBudgetDate, String incomeBudgetDate) {
        this.email = email;
        this.categoryType = categoryType;
        this.expenditureBudgetDate = expenditureBudgetDate;
        this.incomeBudgetDate = incomeBudgetDate;
    }
}
