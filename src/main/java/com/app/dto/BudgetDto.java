package com.app.dto;

import com.app.domain.ExpenditureBudget;
import com.app.domain.IncomeBudget;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BudgetDto {
    private String incomeBudgetAmount;
    private List<BudgetListDto> budgetListDtoList;
    private String expenditureBudgetDate;
    private String incomeBudgetDate;
    private UserDto userDto;

    @Builder
    public BudgetDto(String incomeBudgetAmount, List<BudgetListDto> budgetListDtoList) {
        this.incomeBudgetAmount = incomeBudgetAmount;
        this.budgetListDtoList = budgetListDtoList;
    }

    public IncomeBudget saveIncomeBudget(){
        return IncomeBudget.builder()
                .incomeBudgetAmount(Integer.parseInt(incomeBudgetAmount))
                .incomeBudgetDate(incomeBudgetDate)
                .user(userDto.toEntity())
                .build();
    }


}
