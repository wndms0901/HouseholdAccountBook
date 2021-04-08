package com.app.service;

import com.app.domain.Expenditure;
import com.app.domain.Income;
import com.app.dto.*;
import com.app.mapper.ReportMapper;
import com.app.mapper.WriteMapper;
import com.app.repository.ExpenditureRepository;
import com.app.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService{
    private final ReportMapper reportMapper;

    /**
     * 지출 목록 조회
     * @param reportRequestDto
     * @return MonthReportDto
     */
    @Transactional
    @Override
    public MonthReportDto selectMonthReport(ReportRequestDto reportRequestDto) {
        // 수입/지출 합계 조회
        MonthReportDto result = reportMapper.selectTotalIncomeExpenditure(reportRequestDto);
        // 지난달 일평균 조회
        MonthReportDto dailyAverageLastMonth = reportMapper.selectDailyAverageLastMonth(reportRequestDto);
        // 주별 지출 목록 조회
        List<WeeklyExpenditureDto> weeklyExpenditureDtoList = reportMapper.selectWeeklyExpenditureList(reportRequestDto);
        // 카테고리별 합계 목록 조회
        List<CategoryStatisticsDto> categoryStatisticsDtoList = reportMapper.selectTotalCategoryList(reportRequestDto);
        // 일별 지출 목록 조회
        List<DailyExpenditureDto> dailyExpenditureDtoList = reportMapper.selectDailyExpenditureDtoList(reportRequestDto);
        return MonthReportDto.builder()
                .totalExpenditure(result.getTotalExpenditure())
                .totalIncome(result.getTotalIncome())
                .dailyAverageLastMonth(dailyAverageLastMonth.getDailyAverageLastMonth())
                .weeklyExpenditureDtoList(weeklyExpenditureDtoList)
                .categoryStatisticsDtoList(categoryStatisticsDtoList)
                .dailyExpenditureDtoList(dailyExpenditureDtoList)
                .build();
    }


}
