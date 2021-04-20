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
    private List<AccountCategoryDto> accountCategoryDtoList;

    @Builder
    public CategoryDto(List<LargeCategoryDto> largeCategoryDtoList, List<SmallCategoryDto> smallCategoryDtoList, List<AccountCategoryDto> accountCategoryDtoList) {
        this.largeCategoryDtoList = largeCategoryDtoList;
        this.smallCategoryDtoList = smallCategoryDtoList;
        this.accountCategoryDtoList = accountCategoryDtoList;
    }
}
