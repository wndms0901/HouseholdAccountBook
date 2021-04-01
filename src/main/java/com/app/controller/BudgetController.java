package com.app.controller;

import com.app.dto.*;
import com.app.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/budget")
public class BudgetController {
    private final BudgetService budgetService;

    /**
     * 예산 목록 조회
     * @param categoryType
     * @param incomeBudgetDate
     * @param expenditureBudgetDate
     * @param email
     * @return ResponseEntity<?>
     */
    @GetMapping("/list")
    public ResponseEntity<?> selectBudgetList(@RequestParam String categoryType,
                                                   @RequestParam String incomeBudgetDate,
                                                   @RequestParam String expenditureBudgetDate,
                                                   @RequestParam String email){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        BudgetDto budgetDto = budgetService.selectBudgetList(BudgetRequestDto
                                                                    .builder()
                                                                    .categoryType(categoryType)
                                                                    .incomeBudgetDate(incomeBudgetDate)
                                                                    .expenditureBudgetDate(expenditureBudgetDate)
                                                                    .email(email)
                                                                    .build());
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
}
