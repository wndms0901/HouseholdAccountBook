package com.app.domain;

import com.app.domain.user.User;
import com.app.dto.IncomeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Income extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incomeId;

//    @Column(nullable = false)
//    private String email;

    @Column(nullable = false)
    private String incomeDate;

    @Column(nullable = false)
    private String incomeDescription;

    @Column(nullable = false)
    private int incomeAmount;

    private Long accountCategoryId;

    @Column(nullable = false)
    private Long largeCategoryId;


    private String memo;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @Builder
    public Income(User user, String incomeDescription, int incomeAmount, String incomeDate, Long accountCategoryId, Long largeCategoryId, String memo) {
        this.user = user;
        this.incomeDescription = incomeDescription;
        this.incomeAmount = incomeAmount;
        this.incomeDate = incomeDate;
        this.accountCategoryId = accountCategoryId;
        this.largeCategoryId = largeCategoryId;
        this.memo = memo;
    }

    public void update(IncomeDto incomeDto){
        this.incomeDescription = incomeDto.getIncomeDescription();
        this.incomeAmount = incomeDto.getIncomeAmount();
        this.incomeDate = incomeDto.getIncomeDate();
        this.accountCategoryId = incomeDto.getAccountCategoryId();
        this.largeCategoryId = incomeDto.getLargeCategoryId();
        this.memo = incomeDto.getMemo();
        //return this;
    }

}
