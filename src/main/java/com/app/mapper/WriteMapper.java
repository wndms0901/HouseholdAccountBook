package com.app.mapper;

import com.app.dto.CommonRequestDto;
import com.app.dto.ExpenditureDto;
import com.app.dto.IncomeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WriteMapper {
    /**
     * 지출 목록 조회
     * @param commonRequestDto
     * @return List<ExpenditureDto>
     */
    List<ExpenditureDto> selectExpenditureList(CommonRequestDto commonRequestDto);
    /**
     * 수입 목록 조회
     * @param commonRequestDto
     * @return List<IncomeDto>
     */
    List<IncomeDto> selectIncomeList(CommonRequestDto commonRequestDto);
}
