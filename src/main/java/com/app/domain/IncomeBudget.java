package com.app.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class IncomeBudget extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incomeBudgetId;

    @Column(nullable = false)
    private int incomeBudgetAmount;

    @Column(nullable = false)
    private String incomeBudgetDate;

//    @Column(nullable = false)
//    private String email;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @Builder
    public IncomeBudget(int incomeBudgetAmount, String incomeBudgetDate, User user) {
        this.incomeBudgetAmount = incomeBudgetAmount;
        this.incomeBudgetDate = incomeBudgetDate;
        this.user = user;
    }

    public IncomeBudget update(int incomeBudgetAmount) {
        this.incomeBudgetAmount = incomeBudgetAmount;
        return this;
    }

}
