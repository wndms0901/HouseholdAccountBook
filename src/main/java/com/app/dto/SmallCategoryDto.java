package com.app.dto;

import com.app.domain.SmallCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class SmallCategoryDto {
    private Long smallCategoryId;
    private Long largeCategoryId;
    private String smallCategoryName;

    public SmallCategoryDto(SmallCategory entity) {
        this.smallCategoryId = entity.getSmallCategoryId();
        this.largeCategoryId = entity.getLargeCategory().getLargeCategoryId();
        this.smallCategoryName = entity.getSmallCategoryName();
    }
}
