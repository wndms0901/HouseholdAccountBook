package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IncomeExpenditureDetailDto {
    private Long incomeAmount;
    private Long balanceCarriedForward;
    private Long cash;
    private Long card;
}
