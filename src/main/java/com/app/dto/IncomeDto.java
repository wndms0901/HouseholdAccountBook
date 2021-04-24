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
    private int incomeAmount;
    private Long accountCategoryId;
    private String accountCategoryName;
    private Long largeCategoryId;
    private String largeCategoryName;
    private String memo;
    private UserDto userDto;

//    @Builder
//    public IncomeDto(Long incomeId, String incomeDate, String incomeDescription, int incomeAmount, Long accountCategoryId, Long largeCategoryId, String memo, UserDto userDto) {
//        this.incomeId = incomeId;
//        this.incomeDate = incomeDate;
//        this.incomeDescription = incomeDescription;
//        this.incomeAmount = incomeAmount;
//        this.accountCategoryId = accountCategoryId;
//        this.largeCategoryId = largeCategoryId;
//        this.memo = memo;
//        this.userDto = userDto;
//    }

    public Income toEntity(UserDto userDto){
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
