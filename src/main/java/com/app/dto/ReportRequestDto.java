package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReportRequestDto {
    private String email;
    private String startDate;
    private String endDate;
    private String lastMonthStartDate;
    private String lastMonthEndDate;
    private List<PeriodDto> periodDtoList;
}
