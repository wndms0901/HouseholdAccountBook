package com.app.service;

import com.app.dto.BudgetDto;
import com.app.dto.BudgetListDto;
import com.app.dto.BudgetRequestDto;
import com.app.mapper.BudgetMapper;
import com.app.repository.IncomeBudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BudgetServiceImpl implements BudgetService{
    private final IncomeBudgetRepository incomeBudgetRepository;
    private final BudgetMapper budgetMapper;

    /**
     * 예산 목록 조회
     * @param budgetRequestDto
     * @return BudgetDto
     */
    @Override
    public BudgetDto selectBudgetList(BudgetRequestDto budgetRequestDto) {
        // 수입 예산 금액 조회
        String incomeBudgetAmount = budgetMapper.selectIncomeBudgetAmount(budgetRequestDto);
        if(incomeBudgetAmount==null) incomeBudgetAmount = "-";

        // 카테고리별 예산 목록 조회
        List<BudgetListDto> budgetListDtoList = budgetMapper.selectBudgetList(budgetRequestDto);
        return BudgetDto.builder()
                .incomeBudgetAmount(incomeBudgetAmount)
                .budgetListDtoList(budgetListDtoList)
                .build();
    }

    /**
     * 예산 목록 저장
     * @param budgetDto
     * @return void
     */
    @Override
    public void saveBudgetList(BudgetDto budgetDto) {

    }
}
