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

import java.lang.reflect.Method;
import java.util.ArrayList;
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
        List<DailyExpenditureDto> dailyExpenditureDtoList = reportMapper.selectDailyExpenditureList(reportRequestDto);
        return MonthReportDto.builder()
                .totalExpenditure(result.getTotalExpenditure())
                .totalIncome(result.getTotalIncome())
                .dailyAverageLastMonth(dailyAverageLastMonth.getDailyAverageLastMonth())
                .weeklyExpenditureDtoList(weeklyExpenditureDtoList)
                .categoryStatisticsDtoList(categoryStatisticsDtoList)
                .dailyExpenditureDtoList(dailyExpenditureDtoList)
                .build();
    }

    /**
     * 연간 보고서 조회
     * @param reportRequestDto
     * @return List<YearReportDto>
     */
    @Override
    public List<YearReportDto> selectYearReport(ReportRequestDto reportRequestDto) {
        List<YearReportDto> yearReportDtoList = new ArrayList<YearReportDto>();
        List<PeriodDto> periodDtoList = reportRequestDto.getPeriodDtoList();
        // 월별 수입/지출 목록
        List<List<MonthIncomeExpenditureDto>> monthIncomeExpenditureList = new ArrayList<List<MonthIncomeExpenditureDto>>();

        for(int i=0; i < periodDtoList.size(); i++){
            // 월별 수입/지출 목록 조회
            List<MonthIncomeExpenditureDto> monthIncomeExpenditureDto =  reportMapper.selectMonthIncomeExpenditureList(periodDtoList.get(i));
            monthIncomeExpenditureList.add(monthIncomeExpenditureDto);
//                for(int j=0; j < monthIncomeExpenditureDto.size(); j++){
//                    if(i==0){
//                        // 카테고리 목록 add
//                        yearReportDtoList.add(YearReportDto.builder().category(monthIncomeExpenditureDto.get(j).getLargeCategoryName()).build());
//                        // 첫번째 달 수입/지출 add
//                        yearReportDtoList.get(j).setFifthMonth(monthIncomeExpenditureDto.get(j).getTotal());
//                    }else{
//                        // 월별 수입/지출 add
//                            YearReportDto yearReportDto = yearReportDtoList.get(j);
//
//                    }
//                }
            }
        for(int i=0; i < monthIncomeExpenditureList.get(0).size(); i++){
            YearReportDto yearReportDto = YearReportDto.builder()
                                                       .largeCategoryId(monthIncomeExpenditureList.get(0).get(i).getLargeCategoryId())
                                                       .largeCategoryName(monthIncomeExpenditureList.get(0).get(i).getLargeCategoryName())
                                                       .firstMonth(monthIncomeExpenditureList.get(0).get(i).getTotal())
                                                       .secondMonth(monthIncomeExpenditureList.get(1).get(i).getTotal())
                                                       .thirdMonth(monthIncomeExpenditureList.get(2).get(i).getTotal())
                                                       .fourthMonth(monthIncomeExpenditureList.get(3).get(i).getTotal())
                                                       .fifthMonth(monthIncomeExpenditureList.get(4).get(i).getTotal())
                                                       .sixthMonth(monthIncomeExpenditureList.get(5).get(i).getTotal())
                                                       .seventhMonth(monthIncomeExpenditureList.get(6).get(i).getTotal())
                                                       .eighthMonth(monthIncomeExpenditureList.get(7).get(i).getTotal())
                                                       .ninthMonth(monthIncomeExpenditureList.get(8).get(i).getTotal())
                                                       .tenthMonth(monthIncomeExpenditureList.get(9).get(i).getTotal())
                                                       .eleventhMonth(monthIncomeExpenditureList.get(10).get(i).getTotal())
                                                       .twelfthMonth(monthIncomeExpenditureList.get(11).get(i).getTotal())
                                                       .build();
            yearReportDtoList.add(yearReportDto);
        }
        return yearReportDtoList;
    }
}
