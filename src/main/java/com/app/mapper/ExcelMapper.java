package com.app.mapper;


import com.app.dto.ExpenditureDto;
import com.app.dto.IncomeDto;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ExcelMapper {
    /**
     * 지출 엑셀 업로드 카테고리 ID 조회
     * @param expenditureDto
     * @return ExpenditureDto
     */
    ExpenditureDto selectExpenditureExcelUploadCategoryId(ExpenditureDto expenditureDto);
    /**
     * 수입 엑셀 업로드 카테고리 ID 조회
     * @param incomeDto
     * @return ExpenditureDto
     */
    ExpenditureDto selectIncomeExcelUploadCategoryId(IncomeDto incomeDto);

}