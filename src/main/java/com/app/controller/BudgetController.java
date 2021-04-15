package com.app.controller;

import com.app.dto.*;
import com.app.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/budget")
public class BudgetController {
    private final BudgetService budgetService;

    /**
     * 예산 목록 조회
     * @param budgetRequestDto
     * @return ResponseEntity<?>
     */
    @PostMapping("/list")
    public ResponseEntity<?> selectBudgetList(@RequestBody BudgetRequestDto budgetRequestDto){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        BudgetDto budgetDto = budgetService.selectBudgetList(budgetRequestDto);
        resultMap.put("data", budgetDto);
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    /**
     * 예산 목록 저장
     * @param budgetDto
     * @return ResponseEntity<?>
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveBudgetList(@RequestBody BudgetDto budgetDto) {
        budgetService.saveBudgetList(budgetDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 예산 대비 지출 목록 조회
     * @param budgetRequestDto
     * @return ResponseEntity<?>
     */
    @PostMapping("/expenditure/list")
    public ResponseEntity<?> selectBudgetExpenditureList(@RequestBody BudgetRequestDto budgetRequestDto){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<BudgetExpenditureDto> budgetExpenditureDtoList = budgetService.selectBudgetExpenditureList(budgetRequestDto);
        resultMap.put("data", budgetExpenditureDtoList);
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }
}
