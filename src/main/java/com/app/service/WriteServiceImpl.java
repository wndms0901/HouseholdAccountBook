package com.app.service;

import com.app.domain.Expenditure;
import com.app.domain.Income;
import com.app.dto.*;
import com.app.mapper.WriteMapper;
import com.app.repository.ExpenditureRepository;
import com.app.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WriteServiceImpl implements WriteService{
    private final IncomeRepository incomeRepository;
    private final ExpenditureRepository expenditureRepository;
    private final WriteMapper writeMapper;
    //    private final UserRepository userRepository;

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
