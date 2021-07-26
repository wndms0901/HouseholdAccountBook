package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BudgetRequestDto {
    private String email;
    private String budgetDate;
    private String thisMonthStartDate;
    private String thisMonthEndDate;
    private String threeMonthStartDate;
    private String threeMonthEndDate;
    private String lastMonthStartDate;
    private String lastMonthEndDate;
    private List<PeriodDto> periodDtoList;
}
