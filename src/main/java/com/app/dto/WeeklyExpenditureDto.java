package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WeeklyExpenditureDto {
    private String startDate;
    private String endDate;
    private int weeklyExpenditure;
}
