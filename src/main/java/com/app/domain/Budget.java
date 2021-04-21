package com.app.domain;

import com.app.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Budget extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    @Column(nullable = false)
    private int budgetAmount;

    @Column(nullable = false)
    private String budgetDate;

//    @Column(nullable = false)
//    private String email;

    @Column(nullable = false)
    private Long largeCategoryId;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;


    @Builder
    public Budget(int budgetAmount, String budgetDate, User user, Long largeCategoryId) {
        this.budgetAmount = budgetAmount;
        this.budgetDate = budgetDate;
        this.user = user;
        this.largeCategoryId = largeCategoryId;
    }

    public Budget update(int budgetAmount) {
        this.budgetAmount = budgetAmount;
        return this;
    }

}
