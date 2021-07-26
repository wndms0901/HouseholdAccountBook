package com.app.domain;

import com.app.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Budget extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long budgetAmount;

    @Column(nullable = false, columnDefinition = "char(6)")
    private String budgetDate;

    @Column(nullable = false)
    private Long largeCategoryId;

    @ManyToOne
    @JoinColumn(name = "user_email")
    private User user;

    @Builder
    public Budget(Long budgetAmount, String budgetDate, User user, Long largeCategoryId) {
        this.budgetAmount = budgetAmount;
        this.budgetDate = budgetDate;
        this.user = user;
        this.largeCategoryId = largeCategoryId;
    }

    public Budget update(Long budgetAmount) {
        this.budgetAmount = budgetAmount;
        return this;
    }
}