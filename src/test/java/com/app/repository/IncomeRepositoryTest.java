package com.app.repository;

import com.app.domain.Income;
import com.app.dto.IncomeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class IncomeRepositoryTest {
    @Autowired
    IncomeRepository incomeRepository;

//    @AfterEach
//    public void cleanup(){
//        largeCategoryRepository.deleteAll();
//    }

    @Test
    public void Income_수정(){
       Income income = incomeRepository.findById(1L).get();
        IncomeDto incomeDto = IncomeDto.builder().incomeDescription("테스트").memo("메모메모").build();
       income.update(incomeDto);
       List<Income> all = incomeRepository.findAll();
        assertThat(all.get(0).getIncomeDate()).isEqualTo(income.getIncomeDate());
//        assertThat(all.get(0).getCreatedDate()).isEqualTo(income.getCreatedDate());
//        assertThat(all.get(0).getModifiedDate()).isEqualTo(income.getModifiedDate());
    }

}
