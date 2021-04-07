package com.app.mapper;

import com.app.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {
    /**
     * 수입/지출 합계 조회
     * @param reportRequestDto
     * @return MonthReportDto
     */
    MonthReportDto selectTotalIncomeExpenditure(ReportRequestDto reportRequestDto);
    /**
     * 지난달 일평균 조회
     * @param reportRequestDto
     * @return MonthReportDto
     */
    MonthReportDto selectDailyAverageLastMonth(ReportRequestDto reportRequestDto);
    /**
     * 주별 지출 목록 조회
     * @param reportRequestDto
     * @return List<WeeklyExpenditureDto>
     */
    List<WeeklyExpenditureDto> selectWeeklyExpenditureList(ReportRequestDto reportRequestDto);
    /**
     * 카테고리별 합계 목록 조회
     * @param reportRequestDto
     * @return List<CategoryStatisticsDto>
     */
    List<CategoryStatisticsDto> selectTotalCategoryList(ReportRequestDto reportRequestDto);
    /**
     * 일별 지출 목록 조회
     * @param reportRequestDto
     * @return List<DailyExpenditureDto>
     */
    List<DailyExpenditureDto> selectDailyExpenditureDtoList(ReportRequestDto reportRequestDto);
}
