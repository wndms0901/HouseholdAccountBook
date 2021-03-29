package com.app.dto;

import com.app.domain.AccountCategory;
import lombok.Getter;

@Getter
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
