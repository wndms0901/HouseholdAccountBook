package com.app.service;

import com.app.dto.BudgetDto;
import com.app.mapper.BudgetMapper;
import com.app.repository.IncomeBudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BudgetServiceImpl implements BudgetService{
    private final IncomeBudgetRepository incomeBudgetRepository;
    private final BudgetMapper budgetMapper;

    /**
     * 예산 조회
     * @param
     * @return
     */

    /**
     * 예산 저장
     * @param
     * @return
     */
}
