package com.app.service;

import com.app.domain.Budget;
import com.app.dto.*;
import com.app.mapper.BudgetMapper;
import com.app.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;

    /**
     * 예산 목록 조회
     * @param budgetRequestDto
     * @return BudgetDto
     */
    @Override
    public BudgetDto selectBudgetList(BudgetRequestDto budgetRequestDto) {
        // 수입 합계 조회
        String totalIncome = budgetMapper.selectTotalIncome(budgetRequestDto);

        // 3개월 간 평균 지출 조회
        String threeMonthAverageExpenditure = budgetMapper.selectThreeMonthAverageExpenditure(budgetRequestDto);

        // 지난달 지출 조회
        String lastMonthExpenditure = budgetMapper.selectLastMonthExpenditure(budgetRequestDto);

        // 카테고리별 예산 목록 조회
        List<BudgetListDto> budgetListDtoList = budgetMapper.selectBudgetList(budgetRequestDto);
        return BudgetDto.builder()
                .totalIncome(totalIncome)
                .threeMonthAverageExpenditure(threeMonthAverageExpenditure)
                .lastMonthExpenditure(lastMonthExpenditure)
                .budgetListDtoList(budgetListDtoList)
                .build();
    }

    /**
     * 예산 목록 저장
     * @param budgetDto
     */
    @Transactional
    @Override
    public void saveBudgetList(BudgetDto budgetDto) {
        // 지출 예산 조회
        for (BudgetListDto dto : budgetDto.getBudgetListDtoList()) {
            Optional<Budget> budget = budgetRepository.findByUserEmailAndBudgetDateAndLargeCategoryId(
                    budgetDto.getUserDto().getEmail(),
                    budgetDto.getBudgetDate(),
                    dto.getLargeCategoryId()
            );
            if (budget.isPresent()) {
                // 지출 예산 수정
                budget.get().update(Long.parseLong(dto.getBudgetAmount()));
            } else{
                // 지출 예산 등록
                dto.setBudgetDate(budgetDto.getBudgetDate());
                budgetRepository.save(dto.saveExpenditureBudget(budgetDto.getUserDto()));
            }
        }
    }

    /**
     * 예산 대비 지출 목록 조회
     * @param budgetRequestDto
     * @return List<BudgetExpenditureDto>
     */
    @Override
    public List<BudgetExpenditureDto> selectBudgetExpenditureList(BudgetRequestDto budgetRequestDto) {
        List<BudgetExpenditureDto> budgetExpenditureDtoList = new ArrayList<BudgetExpenditureDto>();
        List<PeriodDto> periodDtoList = budgetRequestDto.getPeriodDtoList();
        // 월별 예산 대비 지출 목록
        List<List<BudgetListDto>> monthBudgetExpenditureList = new ArrayList<List<BudgetListDto>>();

        for(int i=0; i < periodDtoList.size(); i++){
            // 월별 예산 대비 지출 목록 조회
            List<BudgetListDto> budgetListDtoList =  budgetMapper.selectMonthBudgetExpenditureList(periodDtoList.get(i));
            monthBudgetExpenditureList.add(budgetListDtoList);
        }
        for(int i=0; i < monthBudgetExpenditureList.get(0).size(); i++){
            BudgetExpenditureDto budgetExpenditureDto = BudgetExpenditureDto.builder()
                    .largeCategoryId(monthBudgetExpenditureList.get(0).get(i).getLargeCategoryId())
                    .largeCategoryName(monthBudgetExpenditureList.get(0).get(i).getLargeCategoryName())
                    .firstMonth(monthBudgetExpenditureList.get(0).get(i).getTotal())
                    .secondMonth(monthBudgetExpenditureList.get(1).get(i).getTotal())
                    .thirdMonth(monthBudgetExpenditureList.get(2).get(i).getTotal())
                    .fourthMonth(monthBudgetExpenditureList.get(3).get(i).getTotal())
                    .fifthMonth(monthBudgetExpenditureList.get(4).get(i).getTotal())
                    .sixthMonth(monthBudgetExpenditureList.get(5).get(i).getTotal())
                    .seventhMonth(monthBudgetExpenditureList.get(6).get(i).getTotal())
                    .eighthMonth(monthBudgetExpenditureList.get(7).get(i).getTotal())
                    .ninthMonth(monthBudgetExpenditureList.get(8).get(i).getTotal())
                    .tenthMonth(monthBudgetExpenditureList.get(9).get(i).getTotal())
                    .eleventhMonth(monthBudgetExpenditureList.get(10).get(i).getTotal())
                    .twelfthMonth(monthBudgetExpenditureList.get(11).get(i).getTotal())
                    .build();
            budgetExpenditureDtoList.add(budgetExpenditureDto);
        }
        return budgetExpenditureDtoList;
    }
}