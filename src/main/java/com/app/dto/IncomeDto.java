package com.app.dto;

import com.app.domain.Income;
import com.app.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IncomeDto {

    private Long incomeId;
    private String incomeDate;
    private String incomeDescription;
    private Long incomeAmount;
    private Long accountCategoryId;
    private String accountCategoryName;
    private Long largeCategoryId;
    private String largeCategoryName;
    private String memo;
    private UserDto userDto;

    @Builder
    public IncomeDto(Long incomeId, String incomeDate, String incomeDescription, Long incomeAmount, Long accountCategoryId, String accountCategoryName, Long largeCategoryId, String largeCategoryName, String memo, UserDto userDto) {
        this.incomeId = incomeId;
        this.incomeDate = incomeDate;
        this.incomeDescription = incomeDescription;
        this.incomeAmount = incomeAmount;
        this.accountCategoryId = accountCategoryId;
        this.accountCategoryName = accountCategoryName;
        this.largeCategoryId = largeCategoryId;
        this.largeCategoryName = largeCategoryName;
        this.memo = memo;
        this.userDto = userDto;
    }

    public Income toEntity(){
        return Income.builder()
                .user(userDto.toEntity())
                .incomeDate(incomeDate)
                .incomeDescription(incomeDescription)
                .incomeAmount(incomeAmount)
                .accountCategoryId(accountCategoryId)
                .largeCategoryId(largeCategoryId)
                .memo(memo)
                .build();
    }

}
