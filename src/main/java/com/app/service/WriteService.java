package com.app.service;


import com.app.dto.*;

import java.util.List;

public interface WriteService {
    /**
     * 카테고리 목록 조회
     * @param categoryType
     * @return CategoryDto
     */
    CategoryDto selectCategoryList(String categoryType);

    /**
     * 지출 목록 조회
     * @param commonRequestDto
     * @return List<ExpenditureDto>
     */
    List<ExpenditureDto> selectExpenditureList(CommonRequestDto commonRequestDto);
    /**
     * 지출 목록 저장
     * @param expenditureSaveDto
     */
    void saveExpenditureList(ExpenditureSaveDto expenditureSaveDto);

    /**
     * 수입 목록 조회
     * @param commonRequestDto
     * @return List<IncomeDto>
     */
    List<IncomeDto> selectIncomeList(CommonRequestDto commonRequestDto);
    /**
     * 수입 목록 저장
     * @param incomeSaveDto
     */
    void saveIncomeList(IncomeSaveDto incomeSaveDto);
}
