package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IncomeExpenditureDetailDto {
    private int incomeAmount;
    private int balanceCarriedForward;
    private int cash;
    private int card;
}
