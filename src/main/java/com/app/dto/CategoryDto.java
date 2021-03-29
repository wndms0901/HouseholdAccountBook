package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryDto {
    private List<LargeCategoryDto> largeCategoryDtoList;
    private List<SmallCategoryDto> smallCategoryDtoList;

    @Builder
    public CategoryDto(List<LargeCategoryDto> largeCategoryDtoList, List<SmallCategoryDto> smallCategoryDtoList) {
        this.largeCategoryDtoList = largeCategoryDtoList;
        this.smallCategoryDtoList = smallCategoryDtoList;
    }
}
