package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DailyExpenditureDto {
    private int expenditureYear;
    private int expenditureMonth;
    private int expenditureDay;
    private Long dailyExpenditure;

}
