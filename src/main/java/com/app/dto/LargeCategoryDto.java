package com.app.dto;

import com.app.domain.LargeCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LargeCategoryDto {
    private Long largeCategoryId;
    private String largeCategoryName;
    private String categoryType;

    public LargeCategoryDto(LargeCategory entity) {
        this.largeCategoryId = entity.getLargeCategoryId();
        this.largeCategoryName = entity.getLargeCategoryName();
        this.categoryType = entity.getCategoryType();
    }
}
