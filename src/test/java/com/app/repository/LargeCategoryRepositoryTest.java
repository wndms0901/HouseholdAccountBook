package com.app.repository;

import com.app.domain.LargeCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LargeCategoryRepositoryTest {
    @Autowired
    LargeCategoryRepository largeCategoryRepository;

//    @AfterEach
//    public void cleanup(){
//        largeCategoryRepository.deleteAll();
//    }

    @Test
    public void 대분류_카테고리등록_불러오기(){
        // given
        String type = "EXP";
        String name = "식비";
        LargeCategory largeCategory = LargeCategory.builder().categoryType(type).largeCategoryName(name).build();
        largeCategoryRepository.save(largeCategory);

        // when
        List<LargeCategory> largeCategoryList = largeCategoryRepository.findAll();

        // then
        LargeCategory result = largeCategoryList.get(0);
        assertThat(result.getCategoryType()).isEqualTo(type);
        assertThat(result.getLargeCategoryName()).isEqualTo(name);
    }

}
