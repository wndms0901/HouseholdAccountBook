package com.app.controller;

import com.app.dto.ExcelRequestDto;
import com.app.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelService excelService;

    /**
     * 엑셀 다운로드
     * @param excelRequestDto
     * @return ResponseEntity<?>
     */
    @PostMapping("/download")
    public void excelDownload(HttpServletResponse response
                                         , @RequestBody ExcelRequestDto excelRequestDto) {
       if("Expenditure".equals(excelRequestDto.getPageName())){
           // 지출 목록 엑셀 다운로드
            excelService.excelDownExpenditureList(response, excelRequestDto);
       }else if("Income".equals(excelRequestDto.getPageName())){
           // 수입 목록 엑셀 다운로드
            excelService.excelDownIncomeList(response, excelRequestDto);
       }else if("YearReport".equals(excelRequestDto.getPageName())){
           // 연간 보고서 엑셀 다운로드
            excelService.excelDownYearReport(response, excelRequestDto);
       }else if("BudgetWrite".equals(excelRequestDto.getPageName())){
           // 예산 목록 엑셀 다운로드
            excelService.excelDownBudgetList(response, excelRequestDto);
       }else if("BudgetExpenditure".equals(excelRequestDto.getPageName())){
           // 예산 대비 지출 목록 엑셀 다운로드
            excelService.excelDownBudgetExpenditureList(response, excelRequestDto);
       }
    }
}
