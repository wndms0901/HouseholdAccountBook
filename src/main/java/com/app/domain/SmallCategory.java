package com.app.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class SmallCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long smallCategoryId;

    @Column(nullable = false)
    private String smallCategoryName;

    @ManyToOne
    @JoinColumn(name = "largeCategoryId")
    private LargeCategory largeCategory;

    @Builder
    public SmallCategory(String smallCategoryName, LargeCategory largeCategory, String email) {
        this.smallCategoryName = smallCategoryName;
        this.largeCategory = largeCategory;
    }

    public SmallCategory update(String smallCategoryName) {
        this.smallCategoryName = smallCategoryName;
        return this;
    }

}