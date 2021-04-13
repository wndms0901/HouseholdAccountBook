package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MonthIncomeExpenditureDto {
    private int largeCategoryId;
    private String largeCategoryName;
    private int total;
}
