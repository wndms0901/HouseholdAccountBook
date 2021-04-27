package com.app.service;

import com.app.dto.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ExcelService {
    /**
     * 지출 목록 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     * @return void
     */
    void excelDownExpenditureList(HttpServletResponse response, ExcelRequestDto excelRequestDto);
    /**
     * 수입 목록 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     * @return void
     */
    void excelDownIncomeList(HttpServletResponse response, ExcelRequestDto excelRequestDto);
    /**
     * 연간 보고서 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     * @return void
     */
    void excelDownYearReport(HttpServletResponse response, ExcelRequestDto excelRequestDto);
    /**
     * 예산 목록 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     * @return void
     */
    void excelDownBudgetList(HttpServletResponse response, ExcelRequestDto excelRequestDto);
    /**
     * 예산 대비 지출 목록 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     * @return void
     */
    void excelDownBudgetExpenditureList(HttpServletResponse response, ExcelRequestDto excelRequestDto);

}
