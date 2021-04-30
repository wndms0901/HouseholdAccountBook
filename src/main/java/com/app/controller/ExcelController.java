package com.app.controller;


import com.app.dto.ExcelRequestDto;
import com.app.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelService excelService;

    /**
     * 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     */
    @PostMapping("/download")
    public void excelDownload(HttpServletResponse response
            , @RequestBody ExcelRequestDto excelRequestDto) {
        if ("Expenditure".equals(excelRequestDto.getPageName())) {
            // 지출 목록 엑셀 다운로드
            excelService.excelDownExpenditureList(response, excelRequestDto);
        } else if ("Income".equals(excelRequestDto.getPageName())) {
            // 수입 목록 엑셀 다운로드
            excelService.excelDownIncomeList(response, excelRequestDto);
        } else if ("YearReport".equals(excelRequestDto.getPageName())) {
            // 연간 보고서 엑셀 다운로드
            excelService.excelDownYearReport(response, excelRequestDto);
        } else if ("BudgetWrite".equals(excelRequestDto.getPageName())) {
            // 예산 목록 엑셀 다운로드
            excelService.excelDownBudgetList(response, excelRequestDto);
        } else if ("BudgetExpenditure".equals(excelRequestDto.getPageName())) {
            // 예산 대비 지출 목록 엑셀 다운로드
            excelService.excelDownBudgetExpenditureList(response, excelRequestDto);
        }
    }

    /**
     * 엑셀 양식 다운로드
     * @param response
     * @param pageName
     */
    @GetMapping("/form/download")
    public void excelFormDownload(HttpServletResponse response, @RequestParam String pageName) {
       excelService.excelFormDownload(response, pageName);
    }

    /**
     * 엑셀 업로드
     * @param excelRequestDto
     */
    @PostMapping("/upload")
    public ResponseEntity<?> excelUpload(@RequestPart(value="excelRequestDto", required=false) ExcelRequestDto excelRequestDto,
                                         @RequestPart(value="file", required=false) MultipartFile file) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            if ("Expenditure".equals(excelRequestDto.getPageName())) {
                // 지출 목록 엑셀 업로드
                excelService.excelUploadExpenditureList(excelRequestDto, file);
            } else {
                // 수입 목록 엑셀 업로드
                excelService.excelUploadIncomeList(excelRequestDto, file);
            }
            resultMap.put("success", "");
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }catch (Exception e){
            resultMap.put("fail",e.getCause());
            return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }
    }
 }
