package com.app.service;

import com.app.dto.AccountCategoryDto;
import com.app.dto.CategoryDto;
import com.app.dto.LargeCategoryDto;

import java.util.List;

public interface CommonService {
    /**
     * 대분류 카테고리 목록 조회
     * @param categoryType
     * @return List<LargeCategoryDto>
     */
    List<LargeCategoryDto> selectLargeCtgryList(String categoryType);
    /**
     * 카테고리 목록 조회
     * @param categoryType
     * @return CategoryDto
     */
    CategoryDto selectCategoryList(String categoryType);
    /**
     * 통장 카테고리 목록 조회
     * @param accountCategoryType
     * @return List<AccountCategoryDto>
     */
    List<AccountCategoryDto> selectAccountCategoryList(String accountCategoryType);
}
