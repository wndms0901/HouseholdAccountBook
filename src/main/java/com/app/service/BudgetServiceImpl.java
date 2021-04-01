package com.app.service;

import com.app.domain.ExpenditureBudget;
import com.app.domain.IncomeBudget;
import com.app.dto.BudgetDto;
import com.app.dto.BudgetListDto;
import com.app.dto.BudgetRequestDto;
import com.app.mapper.BudgetMapper;
import com.app.repository.ExpenditureBudgetRepository;
import com.app.repository.IncomeBudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BudgetServiceImpl implements BudgetService{
    private final IncomeBudgetRepository incomeBudgetRepository;
    private final ExpenditureBudgetRepository expenditureBudgetRepository;
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
    @Transactional
    @Override
    public void saveBudgetList(BudgetDto budgetDto) {
        // 수입 예산 조회
        Optional<IncomeBudget> incomeBudget = incomeBudgetRepository.findByUserEmailAndIncomeBudgetDate(budgetDto.getUserDto().getEmail(), budgetDto.getIncomeBudgetDate());
        if(incomeBudget.isPresent()){
            // 수입 예산 수정
            incomeBudget.get().update(Integer.parseInt(budgetDto.getIncomeBudgetAmount()));
        }else if(!"-".equals(budgetDto.getIncomeBudgetAmount())){
            // 수입 예산 등록
            incomeBudgetRepository.save(budgetDto.saveIncomeBudget());
        }

        // 지출 예산 조회
        for(BudgetListDto dto : budgetDto.getBudgetListDtoList()){
            Optional<ExpenditureBudget> expenditureBudget = expenditureBudgetRepository.findByUserEmailAndExpenditureBudgetDateAndLargeCategoryId(
                    budgetDto.getUserDto().getEmail(),
                    budgetDto.getExpenditureBudgetDate(),
                    dto.getLargeCategoryId()
            );
            if(expenditureBudget.isPresent()){
                // 지출 예산 수정
                expenditureBudget.get().update(dto.getExpenditureAmount());
            }else{
                // 지출 예산 등록
                dto.setExpenditureBudgetDate(budgetDto.getExpenditureBudgetDate());
                expenditureBudgetRepository.save(dto.saveExpenditureBudget(budgetDto.getUserDto()));
            }
        }


    }}
