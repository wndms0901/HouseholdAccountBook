package com.app.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class ExpenditureBudget extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenditurebudgetId;

    @Column(nullable = false)
    private int expenditureBudgetAmount;

    @Column(nullable = false)
    private String expenditureBudgetDate;

//    @Column(nullable = false)
//    private String email;

    @Column(nullable = false)
    private Long largeCategoryId;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;


    @Builder
    public ExpenditureBudget(int budgetAmount, String expenditureBudgetDate, User user, Long largeCategoryId) {
        this.expenditureBudgetAmount = expenditureBudgetAmount;
        this.expenditureBudgetDate = expenditureBudgetDate;
        this.user = user;
        this.largeCategoryId = largeCategoryId;
    }

    public ExpenditureBudget update(int expenditureBudgetAmount) {
        this.expenditureBudgetAmount = expenditureBudgetAmount;
        return this;
    }

}
