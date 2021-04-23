package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WriteRequestDto {
    private String startDate;
    private String endDate;
    private String incomeDate;
    private String lastMonth;
    //private String email;
    private UserDto userDto;

    @Builder
    public WriteRequestDto(String startDate, String endDate, String incomeDate, String lastMonth, UserDto userDto) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.incomeDate = incomeDate;
        this.lastMonth = lastMonth;
       // this.email = email;
        this.userDto = userDto;
    }
}
