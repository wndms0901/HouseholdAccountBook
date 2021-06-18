package com.app.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class AccountCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountCategoryId;

    @Column(nullable = false)
    private String accountCategoryName;

    @Column(nullable = false, columnDefinition = "char(4)")
    private String accountCategoryType;

    @Builder
    public AccountCategory(String accountCategoryName, String accountCategoryType) {
        this.accountCategoryName = accountCategoryName;
        this.accountCategoryType = accountCategoryType;
    }


}
