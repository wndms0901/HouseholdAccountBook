package com.app.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class LargeCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long largeCategoryId;

    @Column(nullable = false)
    private String largeCategoryName;

    @Column(nullable = false, columnDefinition = "char(3)")
    private String categoryType;

    @OneToMany(mappedBy = "largeCategory")
    private List<SmallCategory> smallCategoryList = new ArrayList<SmallCategory>();

    @Builder
    public LargeCategory(String largeCategoryName, String email, String categoryType) {
        this.largeCategoryName = largeCategoryName;
        this.categoryType = categoryType;
    }

    public List<SmallCategory> getSmallCategoryList() {
        return smallCategoryList;
    }
}
