package com.app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class ExcelRequestDto {
    private WriteRequestDto writeRequestDto;
    private ReportRequestDto reportRequestDto;
    private BudgetRequestDto budgetRequestDto;
    private UserDto userDto;
    private String pageName;
    private String period;
    private String fileNameExtension;
}
