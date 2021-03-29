package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IncomeSaveDto {
    private List<IncomeDto> insertIncomeDtoList;
    private List<IncomeDto> updateIncomeDtoList;
    private List<IncomeDto> deleteIncomeDtoList;
}
