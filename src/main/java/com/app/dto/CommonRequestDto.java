package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommonRequestDto {
    private String startDate;
    private String endDate;
    private String email;

    @Builder
    public CommonRequestDto(String startDate, String endDate, String email) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.email = email;
    }

}
