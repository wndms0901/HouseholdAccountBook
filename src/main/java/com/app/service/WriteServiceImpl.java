package com.app.service;

import com.app.domain.Expenditure;
import com.app.domain.Income;
import com.app.dto.*;
import com.app.mapper.WriteMapper;
import com.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WriteServiceImpl implements WriteService{
    private final LargeCategoryRepository largeCategoryRepository;
    private final SmallCategoryRepository smallCategoryRepository;
    private final AccountCategoryRepository accountCategoryRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenditureRepository expenditureRepository;
    private final WriteMapper writeMapper;
    //    private final UserRepository userRepository;

    /**
     * 카테고리 목록 조회
     * @param categoryType
     * @return CategoryDto
     */
    @Override
    public CategoryDto selectCategoryList(String categoryType) {
        String accountCategoryType ="EXP".equals(categoryType)? "WDRL": "DPST";
        // 대분류 카테고리 조회
        List<LargeCategoryDto> largeCategoryDtoList = writeMapper.selectLargeCategoryList(categoryType);
        //List<LargeCategoryDto> largeCategoryDtoList = largeCategoryRepository.findAllByCategoryType(categoryType).stream().map(LargeCategoryDto::new).collect(Collectors.toList());
        // 통장 카테고리 목록 조회
        List <AccountCategoryDto> accountCategoryDtoList = writeMapper.selectAccountCategoryList(accountCategoryType);
        //List <AccountCategoryDto> accountCategoryDtoList = accountCategoryRepository.findAllByAccountCategoryType(accountCategoryType).stream().map(AccountCategoryDto::new).collect(Collectors.toList());
        CategoryDto categoryDto = CategoryDto.builder().largeCategoryDtoList(largeCategoryDtoList).accountCategoryDtoList(accountCategoryDtoList).build();
        if("EXP".equals(categoryType)) {
            // 소분류 카테고리 조회
            List<SmallCategoryDto> smallCategoryDtoList = writeMapper.selectSmallCategoryList(categoryType);
          //  List<SmallCategoryDto> smallCategoryDtoList = smallCategoryRepository.findAll().stream().map(SmallCategoryDto::new).collect(Collectors.toList());
            categoryDto.setSmallCategoryDtoList(smallCategoryDtoList);
        }
        return categoryDto;
    }

    /**
     * 지출 목록 조회
     * @param commonRequestDto
     * @return List<ExpenditureDto>
     */
    @Override
    public List<ExpenditureDto> selectExpenditureList(CommonRequestDto commonRequestDto) {
        return writeMapper.selectExpenditureList(commonRequestDto);
    }
    /**
     * 지출 목록 저장
     * @param expenditureSaveDto
     */
    @Transactional
    @Override
    public void saveExpenditureList(ExpenditureSaveDto expenditureSaveDto) {
        // 지출 목록 수정
        for(int i=0;i<expenditureSaveDto.getUpdateExpenditureDtoList().size();i++){
            ExpenditureDto expenditureDto = expenditureSaveDto.getUpdateExpenditureDtoList().get(i);
            Expenditure expenditure = expenditureRepository.findById(expenditureDto.getExpenditureId()).get();
            expenditure.update(expenditureDto);
        }
        // 지출 목록 삭제
        if(expenditureSaveDto.getDeleteExpenditureDtoList().size()>0) {
            List<Long> ids = expenditureSaveDto.getDeleteExpenditureDtoList().stream().map(o -> o.getExpenditureId()).collect(Collectors.toList());
            expenditureRepository.deleteAllByIdInQuery(ids);
        }
        // 지출 목록 등록
        if(expenditureSaveDto.getInsertExpenditureDtoList().size()>0) {
            List<Expenditure> insertExpenditureDtoList = expenditureSaveDto.getInsertExpenditureDtoList().stream().map(o -> o.toEntity()).collect(Collectors.toList());
            expenditureRepository.saveAll(insertExpenditureDtoList);
        }
    }

    /**
     * 수입 목록 조회
     * @param commonRequestDto
     * @return List<IncomeDto>
     */
    @Override
    public List<IncomeDto> selectIncomeList(CommonRequestDto commonRequestDto) {
        return writeMapper.selectIncomeList(commonRequestDto);
    }
    /**
     * 수입 목록 저장
     * @param incomeSaveDto
     */
    @Transactional
    @Override
    public void saveIncomeList(IncomeSaveDto incomeSaveDto) {
        // 수입 목록 수정
        for(int i=0;i<incomeSaveDto.getUpdateIncomeDtoList().size();i++){
            IncomeDto incomeDto = incomeSaveDto.getUpdateIncomeDtoList().get(i);
            Income income = incomeRepository.findById(incomeDto.getIncomeId()).get();
            income.update(incomeDto);
        }
        // 수입 목록 삭제
        if(incomeSaveDto.getDeleteIncomeDtoList().size()>0) {
            List<Long> ids = incomeSaveDto.getDeleteIncomeDtoList().stream().map(o -> o.getIncomeId()).collect(Collectors.toList());
            incomeRepository.deleteAllByIdInQuery(ids);
        }
        // 수입 목록 등록
        if(incomeSaveDto.getInsertIncomeDtoList().size()>0) {
            List<Income> insertIncomeDtoList = incomeSaveDto.getInsertIncomeDtoList().stream().map(o -> o.toEntity()).collect(Collectors.toList());
            incomeRepository.saveAll(insertIncomeDtoList);
        }
    }
}
