package com.app.dto;

import com.app.domain.AccountCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountCategoryDto {

    private Long accountCategoryId;
    private String accountCategoryName;
    private String accountCategoryType;

    public AccountCategoryDto(AccountCategory entity) {
        this.accountCategoryId = entity.getAccountCategoryId();
        this.accountCategoryName = entity.getAccountCategoryName();
        this.accountCategoryType = entity.getAccountCategoryType();
    }
}
