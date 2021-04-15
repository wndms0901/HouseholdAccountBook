package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PeriodDto {
    private String year;
    private String month;
    private String week;
    private String startDate;
    private String endDate;
    private String email;
}
