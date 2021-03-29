package com.app.domain;

import com.app.dto.ExpenditureDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Expenditure extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenditureId;

//    @Column(nullable = false)
//    private String email;

    @Column(nullable = false)
    private String expenditureDate;

    @Column(nullable = false)
    private String expenditureDescription;

    @Column(nullable = false)
    private int cash;

    @Column(nullable = false)
    private int card;

    private Long accountCategoryId;

    @Column(nullable = false)
    private Long largeCategoryId;

    @Column(nullable = false)
    private Long smallCategoryId;

    private String memo;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @Builder
    public Expenditure(User user, String expenditureDate, String expenditureDescription, int cash, int card, Long accountCategoryId, Long largeCategoryId, Long smallCategoryId, String memo) {
        this.user = user;
        this.expenditureDate = expenditureDate;
        this.expenditureDescription = expenditureDescription;
        this.cash = cash;
        this.card = card;
        this.accountCategoryId = accountCategoryId;
        this.largeCategoryId = largeCategoryId;
        this.smallCategoryId = smallCategoryId;
        this.memo = memo;
    }

    public void update(ExpenditureDto expenditureDto) {
        this.expenditureDate = expenditureDto.getExpenditureDate();
        this.expenditureDescription = expenditureDto.getExpenditureDescription();
        this.cash = expenditureDto.getCash();
        this.card = expenditureDto.getCard();
        this.accountCategoryId = expenditureDto.getAccountCategoryId();
        this.largeCategoryId = expenditureDto.getLargeCategoryId();
        this.smallCategoryId = expenditureDto.getSmallCategoryId();
        this.memo = expenditureDto.getMemo();
        //return this;
    }

}
