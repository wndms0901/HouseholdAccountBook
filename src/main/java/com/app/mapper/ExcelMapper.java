package com.app.mapper;


import com.app.dto.ExpenditureDto;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ExcelMapper {
    /**
     * 카테고리 ID 조회
     * @param expenditureDto
     * @return ExpenditureDto
     */
    ExpenditureDto selectCategoryId(ExpenditureDto expenditureDto);


}