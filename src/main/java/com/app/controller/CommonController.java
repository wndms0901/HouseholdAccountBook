package com.app.controller;

import com.app.dto.AccountCategoryDto;
import com.app.dto.CategoryDto;
import com.app.dto.LargeCategoryDto;
import com.app.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/common")
public class CommonController {
    private final CommonService commonService;

    /**
     * 대분류 카테고리 목록 조회
     * @param categoryType
     * @return ResponseEntity<?>
     */
    @GetMapping("/large-category/list")
    public ResponseEntity<?> selectLargeCtgryList(@RequestParam String categoryType) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<LargeCategoryDto> list = commonService.selectLargeCtgryList(categoryType);
        resultMap.put("data", list);
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
        CategoryDto result = commonService.selectCategoryList(categoryType);
        resultMap.put("data", result);
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }

    /**
     * 통장 카테고리 목록 조회
     * @param accountCategoryType
     * @return ResponseEntity<?>
     */
    @GetMapping("/account-category/list")
    public ResponseEntity<?> selectAccountCtgryList(@RequestParam String accountCategoryType) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<AccountCategoryDto> list = commonService.selectAccountCategoryList(accountCategoryType);
        System.out.println(">>>>>"+list.size());
        resultMap.put("data", list);
        return new ResponseEntity(resultMap, HttpStatus.OK);
    }
}
