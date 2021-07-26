package com.app.controller;

import com.app.dto.*;
import com.app.service.WriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/write")
public class WriteController {
    private final WriteService writeService;

    /**
     * 수입/지출 상세 조회
     * @param writeRequestDto
     * @return ResponseEntity<?>
     */
    @PostMapping("/income-expenditure/detail")
    public ResponseEntity<?> selectIncomeExpenditureDetail(@RequestBody WriteRequestDto writeRequestDto){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        IncomeExpenditureDetailDto dto = writeService.selectIncomeExpenditureDetail(writeRequestDto);
        resultMap.put("data", dto);
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    /**
     * 카테고리 목록 조회
     * @param categoryType
     * @return ResponseEntity<?>
     */
    @GetMapping("/category/list")
    public ResponseEntity<?> selectCategoryList(@RequestParam String categoryType) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        CategoryDto result = writeService.selectCategoryList(categoryType);
        resultMap.put("data", result);
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    /**
     * 지출 목록 조회
     * @param startDate
     * @param endDate
     * @param email
     * @return ResponseEntity<?>
     */
    @GetMapping("/expenditure/list")
    public ResponseEntity<?> selectExpenditureList(@RequestParam String startDate,
                                              @RequestParam String endDate,
                                              @RequestParam String email){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<ExpenditureDto> list = writeService.selectExpenditureList(WriteRequestDto.builder().startDate(startDate).endDate(endDate).userDto(UserDto.builder().email(email).build()).build());
        resultMap.put("data", list);
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    /**
     * 지출 목록 저장
     * @param expenditureSaveDto
     * @return ResponseEntity<?>
     */
    @PostMapping("/expenditure/save")
    public ResponseEntity<?> saveExpenditureList(@RequestBody ExpenditureSaveDto expenditureSaveDto) {
        writeService.saveExpenditureList(expenditureSaveDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 정산 저장
     * @param writeRequestDto
     * @return ResponseEntity<?>
     */
    @PostMapping("/calculation/save")
    public ResponseEntity<?> saveCalculation(@RequestBody WriteRequestDto writeRequestDto) {
        writeService.saveCalculation(writeRequestDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 수입 목록 조회
     * @param startDate
     * @param endDate
     * @param email
     * @return ResponseEntity<?>
     */
    @GetMapping("/income/list")
    public ResponseEntity<?> selectIncomeList(@RequestParam String startDate,
                                              @RequestParam String endDate,
                                              @RequestParam String email){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<IncomeDto> list = writeService.selectIncomeList(WriteRequestDto.builder().startDate(startDate).endDate(endDate).userDto(UserDto.builder().email(email).build()).build());
        resultMap.put("data", list);
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    /**
     * 수입 목록 저장
     * @param incomeSaveDto
     * @return ResponseEntity<?>
     */
    @PostMapping("/income/save")
    public ResponseEntity<?> saveIncomeList(@RequestBody IncomeSaveDto incomeSaveDto) {
        writeService.saveIncomeList(incomeSaveDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
