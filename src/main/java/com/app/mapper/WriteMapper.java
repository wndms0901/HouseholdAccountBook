package com.app.mapper;

import com.app.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WriteMapper {
    /**
     * 대분류 카테고리 목록 조회
     * @param categoryType
     * @return List<LargeCategoryDto>
     */
    List<LargeCategoryDto> selectLargeCategoryList(String categoryType);
    /**
     * 소분류 카테고리 목록 조회
     * @param categoryType
     * @return List<SmallCategoryDto>
     */
    List<SmallCategoryDto> selectSmallCategoryList(String categoryType);
    /**
     * 통장 카테고리 목록 조회
     * @param accountCategoryType
     * @return List<AccountCategoryDto>
     */
    List<AccountCategoryDto> selectAccountCategoryList(String accountCategoryType);
    /**
     * 지출 목록 조회
     * @param writeRequestDto
     * @return List<ExpenditureDto>
     */
    List<ExpenditureDto> selectExpenditureList(WriteRequestDto writeRequestDto);
    /**
     * 정산 목록 조회
     * @param writeRequestDto
     * @return List<IncomeDto>
     */
    List<IncomeDto> selectCalculationList(WriteRequestDto writeRequestDto);
    /**
     * 수입 목록 조회
     * @param writeRequestDto
     * @return List<IncomeDto>
     */
    List<IncomeDto> selectIncomeList(WriteRequestDto writeRequestDto);
}
