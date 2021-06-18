package com.app.dto;

import com.app.domain.Expenditure;
import com.app.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExpenditureDto {

    private Long expenditureId;
    private String expenditureDate;
    private String expenditureDescription;
    private Long cash;
    private Long card;
    private Long accountCategoryId;
    private String accountCategoryName;
    private Long largeCategoryId;
    private String largeCategoryName;
    private Long smallCategoryId;
    private String smallCategoryName;
    private String memo;
    private UserDto userDto;

    @Builder
    public ExpenditureDto(Long expenditureId, String expenditureDate, String expenditureDescription, Long cash, Long card, Long accountCategoryId, String accountCategoryName, Long largeCategoryId, String largeCategoryName, Long smallCategoryId, String smallCategoryName, String memo, UserDto userDto) {
        this.expenditureId = expenditureId;
        this.expenditureDate = expenditureDate;
        this.expenditureDescription = expenditureDescription;
        this.cash = cash;
        this.card = card;
        this.accountCategoryId = accountCategoryId;
        this.accountCategoryName = accountCategoryName;
        this.largeCategoryId = largeCategoryId;
        this.largeCategoryName = largeCategoryName;
        this.smallCategoryId = smallCategoryId;
        this.smallCategoryName = smallCategoryName;
        this.memo = memo;
        this.userDto = userDto;
    }

    public Expenditure toEntity() {
        User userEntity = userDto==null?null:userDto.toEntity();
        return Expenditure.builder()
                .user(userEntity)
                .expenditureDate(expenditureDate)
                .expenditureDescription(expenditureDescription)
                .cash(cash)
                .card(card)
                .accountCategoryId(accountCategoryId)
                .largeCategoryId(largeCategoryId)
                .smallCategoryId(smallCategoryId)
                .memo(memo)
                .build();
    }

}
