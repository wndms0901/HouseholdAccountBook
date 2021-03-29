package com.app.mapper;

import com.app.dto.CommonRequestDto;
import com.app.dto.ExpenditureDto;
import com.app.dto.IncomeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WriteMapper {
    List<ExpenditureDto> selectExpenditureList(CommonRequestDto commonRequestDto);
    List<IncomeDto> selectIncomeList(CommonRequestDto commonRequestDto);
}
