package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class YearReportDto {
    private String largeCategoryId;
    private String largeCategoryName;
    private Long firstMonth;
    private Long secondMonth;
    private Long thirdMonth;
    private Long fourthMonth;
    private Long fifthMonth;
    private Long sixthMonth;
    private Long seventhMonth;
    private Long eighthMonth;
    private Long ninthMonth;
    private Long tenthMonth;
    private Long eleventhMonth;
    private Long twelfthMonth;
    private Long total;

    @Builder
    public YearReportDto(String largeCategoryId, String largeCategoryName, Long firstMonth, Long secondMonth, Long thirdMonth, Long fourthMonth, Long fifthMonth, Long sixthMonth, Long seventhMonth, Long eighthMonth, Long ninthMonth, Long tenthMonth, Long eleventhMonth, Long twelfthMonth) {
        this.largeCategoryId = largeCategoryId;
        this.largeCategoryName = largeCategoryName;
        this.firstMonth = firstMonth;
        this.secondMonth = secondMonth;
        this.thirdMonth = thirdMonth;
        this.fourthMonth = fourthMonth;
        this.fifthMonth = fifthMonth;
        this.sixthMonth = sixthMonth;
        this.seventhMonth = seventhMonth;
        this.eighthMonth = eighthMonth;
        this.ninthMonth = ninthMonth;
        this.tenthMonth = tenthMonth;
        this.eleventhMonth = eleventhMonth;
        this.twelfthMonth = twelfthMonth;
        this.total = firstMonth + secondMonth + thirdMonth + fourthMonth + fifthMonth + sixthMonth + seventhMonth + eighthMonth
                + ninthMonth + tenthMonth + eleventhMonth + twelfthMonth;
    }

}
