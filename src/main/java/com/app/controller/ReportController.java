package com.app.controller;

import com.app.dto.*;
import com.app.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;

    /**
     * 월 보고서 조회
     * @param reportRequestDto
     * @return ResponseEntity<?>
     */
    @PostMapping("/month")
    public ResponseEntity<?> selectMonthReport(@RequestBody ReportRequestDto reportRequestDto){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        MonthReportDto result = reportService.selectMonthReport(reportRequestDto);
        resultMap.put("data", result);
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    /**
     * 연간 보고서 조회
     * @param reportRequestDto
     * @return ResponseEntity<?>
     */
    @PostMapping("/year")
    public ResponseEntity<?> selectYearReport(@RequestBody ReportRequestDto reportRequestDto){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<YearReportDto> result = reportService.selectYearReport(reportRequestDto);
        resultMap.put("data", result);
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }
}
