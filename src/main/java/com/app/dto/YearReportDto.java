package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class YearReportDto {
    private int largeCategoryId;
    private String largeCategoryName;
    private int firstMonth;
    private int secondMonth;
    private int thirdMonth;
    private int fourthMonth;
    private int fifthMonth;
    private int sixthMonth;
    private int seventhMonth;
    private int eighthMonth;
    private int ninthMonth;
    private int tenthMonth;
    private int eleventhMonth;
    private int twelfthMonth;
    private int total;

    @Builder
    public YearReportDto(int largeCategoryId, String largeCategoryName, int firstMonth, int secondMonth, int thirdMonth, int fourthMonth, int fifthMonth, int sixthMonth, int seventhMonth, int eighthMonth, int ninthMonth, int tenthMonth, int eleventhMonth, int twelfthMonth) {
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
