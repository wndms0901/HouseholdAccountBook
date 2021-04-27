package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExcelRequestDto {
    private WriteRequestDto writeRequestDto;
    private ReportRequestDto reportRequestDto;
    private BudgetRequestDto budgetRequestDto;
    private String pageName;
    private String period;
}
