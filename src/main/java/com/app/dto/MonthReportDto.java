package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MonthReportDto {
    private int totalExpenditure;
    private int totalIncome;
    private int dailyAverageLastMonth;
    private List<WeeklyExpenditureDto> weeklyExpenditureDtoList;
    private List<CategoryStatisticsDto> categoryStatisticsDtoList;
    private List<DailyExpenditureDto> dailyExpenditureDtoList;
}
