package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportRequestDto {
    private String email;
    private String startDate;
    private String endDate;
    private String lastMonthStartDate;
    private String lastMonthEndDate;

}
