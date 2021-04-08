package com.app.dto;

import lombok.Builder;
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

    @Builder
    public MonthReportDto(int totalExpenditure, int totalIncome, int dailyAverageLastMonth, List<WeeklyExpenditureDto> weeklyExpenditureDtoList, List<CategoryStatisticsDto> categoryStatisticsDtoList, List<DailyExpenditureDto> dailyExpenditureDtoList) {
        this.totalExpenditure = totalExpenditure;
        this.totalIncome = totalIncome;
        this.dailyAverageLastMonth = dailyAverageLastMonth;
        this.weeklyExpenditureDtoList = weeklyExpenditureDtoList;
        this.categoryStatisticsDtoList = categoryStatisticsDtoList;
        this.dailyExpenditureDtoList = dailyExpenditureDtoList;
    }
}
