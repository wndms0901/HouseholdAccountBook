package com.app.dto;

import com.app.domain.Expenditure;
import com.app.domain.User;
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
    private int cash;
    private int card;
    private Long accountCategoryId;
    private Long largeCategoryId;
    private Long smallCategoryId;
    private String smallCategoryName;
    private String memo;
    private UserDto userDto;

    @Builder
    public ExpenditureDto(Long expenditureId, String expenditureDate, String expenditureDescription, int cash, int card, Long accountCategoryId, Long largeCategoryId, Long smallCategoryId, String smallCategoryName, String memo, UserDto userDto) {
        this.expenditureId = expenditureId;
        this.expenditureDate = expenditureDate;
        this.expenditureDescription = expenditureDescription;
        this.cash = cash;
        this.card = card;
        this.accountCategoryId = accountCategoryId;
        this.largeCategoryId = largeCategoryId;
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
