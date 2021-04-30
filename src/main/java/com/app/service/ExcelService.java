package com.app.service;

import com.app.dto.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface ExcelService {
    /**
     * 지출 목록 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     */
    void excelDownExpenditureList(HttpServletResponse response, ExcelRequestDto excelRequestDto);
    /**
     * 수입 목록 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     */
    void excelDownIncomeList(HttpServletResponse response, ExcelRequestDto excelRequestDto);
    /**
     * 연간 보고서 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     */
    void excelDownYearReport(HttpServletResponse response, ExcelRequestDto excelRequestDto);
    /**
     * 예산 목록 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     */
    void excelDownBudgetList(HttpServletResponse response, ExcelRequestDto excelRequestDto);
    /**
     * 예산 대비 지출 목록 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     */
    void excelDownBudgetExpenditureList(HttpServletResponse response, ExcelRequestDto excelRequestDto);
    /**
     * 엑셀 양식 다운로드
     * @param response
     * @param pageName
     */
    void excelFormDownload(HttpServletResponse response, String pageName);

    /**
     * 지출 목록 엑셀 업로드
     *
     * @param excelRequestDto
     */
    void excelUploadExpenditureList(ExcelRequestDto excelRequestDto, MultipartFile file) ;
    /**
     * 수입 목록 엑셀 업로드
     * @param excelRequestDto
     */
    void excelUploadIncomeList(ExcelRequestDto excelRequestDto, MultipartFile file) throws Exception;
}
