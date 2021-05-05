package com.app.service;


import com.app.dto.*;

import java.util.List;

public interface WriteService {
    /**
     * 수입/지출 상세 조회
     * @param writeRequestDto
     * @return IncomeExpenditureDetailDto
     */
    IncomeExpenditureDetailDto selectIncomeExpenditureDetail(WriteRequestDto writeRequestDto);
    /**
     * 카테고리 목록 조회
     * @param categoryType
     * @return CategoryDto
     */
    CategoryDto selectCategoryList(String categoryType);

    /**
     * 지출 목록 조회
     * @param writeRequestDto
     * @return List<ExpenditureDto>
     */
    List<ExpenditureDto> selectExpenditureList(WriteRequestDto writeRequestDto);
    /**
     * 지출 목록 저장
     * @param expenditureSaveDto
     */
    void saveExpenditureList(ExpenditureSaveDto expenditureSaveDto);
    /**
     * 정산 저장
     * @param writeRequestDto
     */
    void saveCalculation(WriteRequestDto writeRequestDto);

    /**
     * 수입 목록 조회
     * @param writeRequestDto
     * @return List<IncomeDto>
     */
    List<IncomeDto> selectIncomeList(WriteRequestDto writeRequestDto);
    /**
     * 수입 목록 저장
     * @param incomeSaveDto
     */
    void saveIncomeList(IncomeSaveDto incomeSaveDto);
}
