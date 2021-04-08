package com.app.service;

import com.app.dto.LargeCategoryDto;
import com.app.dto.MonthReportDto;
import com.app.dto.ReportRequestDto;

import java.util.List;

public interface ReportService {
    /**
     * 월 보고서 조회
     * @param reportRequestDto
     * @return MonthReportDto
     */
    MonthReportDto selectMonthReport(ReportRequestDto reportRequestDto);
}
