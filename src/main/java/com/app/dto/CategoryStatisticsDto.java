package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryStatisticsDto {
    private Long largeCategoryId;
    private String largeCategoryName;
    private String categoryType;
    private Long totalCategory;
}
