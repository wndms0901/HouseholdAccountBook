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
    private String categoryType;
    private String expenditureBudgetDate;
    private String incomeBudgetDate;
    private List<PeriodDto> periodDtoList;

}
