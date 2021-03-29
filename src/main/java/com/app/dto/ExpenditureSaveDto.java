package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class ExpenditureSaveDto {
    private List<ExpenditureDto> insertExpenditureDtoList;
    private List<ExpenditureDto> updateExpenditureDtoList;
    private List<ExpenditureDto> deleteExpenditureDtoList;
}
