package com.app.service;

import com.app.dto.AccountCategoryDto;
import com.app.dto.CategoryDto;
import com.app.dto.LargeCategoryDto;
import com.app.dto.SmallCategoryDto;
import com.app.repository.AccountCategoryRepository;
import com.app.repository.LargeCategoryRepository;
import com.app.repository.SmallCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommonServiceImpl implements CommonService{
    private final LargeCategoryRepository largeCategoryRepository;
    private final SmallCategoryRepository smallCategoryRepository;
    private final AccountCategoryRepository accountCategoryRepository;

    /**
     * 대분류 카테고리 목록 조회
     * @param categoryType
     * @return List<LargeCategoryDto>
     */
    @Transactional
    @Override
    public List<LargeCategoryDto> selectLargeCtgryList(String categoryType) {
        return largeCategoryRepository.findAllByCategoryType(categoryType).stream().map(LargeCategoryDto::new).collect(Collectors.toList());
    }
    /**
     * 카테고리 목록 조회
     * @param categoryType
     * @return CategoryDto
     */
    @Transactional
    @Override
    public CategoryDto selectCategoryList(String categoryType) {
        // 대분류 카테고리 조회
        List<LargeCategoryDto> largeCategoryDtoList = largeCategoryRepository.findAllByCategoryType(categoryType).stream().map(LargeCategoryDto::new).collect(Collectors.toList());
        // 소분류 카테고리 조회
        List<SmallCategoryDto> smallCategoryDtoList = smallCategoryRepository.findAll().stream().map(SmallCategoryDto::new).collect(Collectors.toList());
        return CategoryDto.builder().largeCategoryDtoList(largeCategoryDtoList).smallCategoryDtoList(smallCategoryDtoList).build();
    }

    /**
     * 통장 카테고리 목록 조회
     * @param accountCategoryType
     * @return List<AccountCategoryDto>
     */
    @Transactional
    @Override
    public List<AccountCategoryDto> selectAccountCategoryList(String accountCategoryType) {
        return accountCategoryRepository.findAllByAccountCategoryType(accountCategoryType).stream().map(AccountCategoryDto::new).collect(Collectors.toList());
    }
}
