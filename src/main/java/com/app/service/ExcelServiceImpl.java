package com.app.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.app.domain.Expenditure;
import com.app.domain.Income;
import com.app.dto.*;
import com.app.mapper.ExcelMapper;
import com.app.mapper.WriteMapper;
import com.app.repository.ExpenditureRepository;
import com.app.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ExcelServiceImpl implements ExcelService{
    private final ExpenditureRepository expenditureRepository;
    private final IncomeRepository incomeRepository;
    private final WriteMapper writeMapper;
    private final ExcelMapper excelMapper;
    private final ReportService reportService;
    private final BudgetService budgetService;
    @Value("${spring.aws.bucket.name}")
    private String AWS_BUCKETNAME;
    @Value("${spring.aws.access.key}")
    private String AWS_ACCESS_KEY;
    @Value("${spring.aws.secret.key}")
    private String AWS_SECRET_KEY;

    /**
     * ?????? ?????? ?????? ????????????
     * @param response
     * @param excelRequestDto
     */
    @Override
    public void excelDownExpenditureList(HttpServletResponse response, ExcelRequestDto excelRequestDto) {
        // ?????? ?????? ????????? ????????????
        Workbook workbook = new SXSSFWorkbook();
        // ?????? ?????? ????????? Sheet ??? ?????? ??????????????? (?????? ?????? ???????????? ?????? Sheet??? ?????? ??? ????????????)
        Sheet sheet = workbook.createSheet();
        // ??? ??? ??????
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 7000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 7000);

        // ?????? ???????????? ????????? DTO??? ???????????????
        List<ExpenditureDto> list = writeMapper.selectExpenditureList(excelRequestDto.getWriteRequestDto());

        // ????????? ???????????????
        int rowIndex = 0;
        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell  = titleRow.createCell(0);
        titleCell.setCellValue("?????? ??????");
        titleCell.setCellStyle(applyTextCellStyle(workbook,"title"));

        Row dateRow = sheet.createRow(rowIndex++);
        Cell dateCell  = dateRow.createCell(0);
        dateCell.setCellValue(excelRequestDto.getPeriod());
        dateCell.setCellStyle(applyTextCellStyle(workbook,"title"));
        rowIndex++;

        Row headerRow = sheet.createRow(rowIndex++);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("??????");
        headerCell1.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("????????????");
        headerCell2.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("??????");
        headerCell3.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell4 = headerRow.createCell(3);
        headerCell4.setCellValue("??????");
        headerCell4.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell5 = headerRow.createCell(4);
        headerCell5.setCellValue("????????????");
        headerCell5.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell6 = headerRow.createCell(5);
        headerCell6.setCellValue("?????????");
        headerCell6.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell7 = headerRow.createCell(6);
        headerCell7.setCellValue("?????????");
        headerCell7.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell8 = headerRow.createCell(7);
        headerCell8.setCellValue("??????");
        headerCell8.setCellStyle(applyTextCellStyle(workbook,"header"));

        if(list.size()>0) {
            // ????????? ???????????? ???????????????
            for (ExpenditureDto dto : list) {
                Row bodyRow = sheet.createRow(rowIndex++);

                Cell bodyCell1 = bodyRow.createCell(0);
                bodyCell1.setCellValue(Integer.parseInt(dto.getExpenditureDate()));
                bodyCell1.setCellStyle(applyTextCellStyle(workbook, "date"));

                Cell bodyCell2 = bodyRow.createCell(1);
                bodyCell2.setCellValue(dto.getExpenditureDescription());
                bodyCell2.setCellStyle(applyTextCellStyle(workbook, "")); // ???????????? ??????

                Cell bodyCell3 = bodyRow.createCell(2);
                bodyCell3.setCellValue(dto.getCash());
                bodyCell3.setCellStyle(applyNumberCellStyle(workbook, "writePrice"));

                Cell bodyCell4 = bodyRow.createCell(3);
                bodyCell4.setCellValue(dto.getCard());
                bodyCell4.setCellStyle(applyNumberCellStyle(workbook, "writePrice"));

                Cell bodyCell5 = bodyRow.createCell(4);
                bodyCell5.setCellValue(dto.getAccountCategoryName());
                bodyCell5.setCellStyle(applyNumberCellStyle(workbook, "writePrice"));

                Cell bodyCell6 = bodyRow.createCell(5);
                bodyCell6.setCellValue(dto.getLargeCategoryName());
                bodyCell6.setCellStyle(applyTextCellStyle(workbook, "center"));

                Cell bodyCell7 = bodyRow.createCell(6);
                bodyCell7.setCellValue(dto.getSmallCategoryName());
                bodyCell7.setCellStyle(applyTextCellStyle(workbook, "center"));

                Cell bodyCell8 = bodyRow.createCell(7);
                bodyCell8.setCellValue(dto.getMemo());
                bodyCell8.setCellStyle(applyTextCellStyle(workbook, "")); // ???????????? ??????
            }
        }else{
            // ????????? ???????????? ?????????
            Row bodyRow = sheet.createRow(rowIndex);
            for (int i=0; i<8; i++) {
                Cell bodyCell = bodyRow.createCell(i);
                if(i==0){bodyCell.setCellValue(excelRequestDto.getPeriod()+" ????????? ???????????? ??????????????? ????????????.");}
                bodyCell.setCellStyle(applyTextCellStyle(workbook, "center"));
            }
            //??? ??????
            sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,0,7));
            rowIndex++;
        }
            // ??????
            Row totalRow = sheet.createRow(rowIndex);
            Cell totalCell1 = totalRow.createCell(0);
            totalCell1.setCellValue("?????? ??????");
            totalCell1.setCellStyle(applyTextCellStyle(workbook, "expenditureTotal"));

            Cell totalCell2 = totalRow.createCell(1);
            totalCell2.setCellStyle(applyTextCellStyle(workbook, "expenditureTotal"));

            Cell totalCell3 = totalRow.createCell(2);
            totalCell3.setCellStyle(applyNumberCellStyle(workbook, "expenditureTotal"));
            totalCell3.setCellFormula("SUM(C5:C" + (5 + list.size() - 1) + ")");
            //int totalCash = list.size()>0? list.stream().mapToInt(ExpenditureDto::getCash).sum() : 0;
            //totalCell3.setCellValue(totalCash);

            Cell totalCell4 = totalRow.createCell(3);
            totalCell4.setCellStyle(applyNumberCellStyle(workbook, "expenditureTotal"));
            totalCell4.setCellFormula("SUM(D5:D" + (5 + list.size() - 1) + ")");
            //int totalCard = list.size()>0?list.stream().mapToInt(ExpenditureDto::getCard).sum() : 0;
            //totalCell4.setCellValue(totalCard);

            Cell totalCell5 = totalRow.createCell(4);
            totalCell5.setCellStyle(applyTextCellStyle(workbook,"expenditureTotal"));
            Cell totalCell6 = totalRow.createCell(5);
            totalCell6.setCellStyle(applyTextCellStyle(workbook,"expenditureTotal"));
            Cell totalCell7 = totalRow.createCell(6);
            totalCell7.setCellStyle(applyTextCellStyle(workbook,"expenditureTotal"));
            Cell totalCell8 = totalRow.createCell(7);
            totalCell8.setCellStyle(applyTextCellStyle(workbook,"expenditureTotal"));

        // ??? ??????
        sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,0,1)); //?????????, ?????????, ?????????, ????????? (??????????????? ?????? 0?????? ??????)
        // ????????? ??????
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        try {
            // ?????? ??????
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ?????? ?????? ?????? ????????????
     * @param response
     * @param excelRequestDto
     */
    @Override
    public void excelDownIncomeList(HttpServletResponse response, ExcelRequestDto excelRequestDto) {
        // ?????? ?????? ????????? ????????????
        Workbook workbook = new SXSSFWorkbook();
        // ?????? ?????? ????????? Sheet ??? ?????? ??????????????? (?????? ?????? ???????????? ?????? Sheet??? ?????? ??? ????????????)
        Sheet sheet = workbook.createSheet();
        // ??? ??? ??????
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 7000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 7000);

        // ?????? ???????????? ????????? DTO??? ???????????????
        List<IncomeDto> list = writeMapper.selectIncomeList(excelRequestDto.getWriteRequestDto());

        // ????????? ???????????????
        int rowIndex = 0;
        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell  = titleRow.createCell(0);
        titleCell.setCellValue("?????? ??????");
        titleCell.setCellStyle(applyTextCellStyle(workbook,"title"));

        Row dateRow = sheet.createRow(rowIndex++);
        Cell dateCell  = dateRow.createCell(0);
        dateCell.setCellValue(excelRequestDto.getPeriod());
        dateCell.setCellStyle(applyTextCellStyle(workbook,"title"));
        rowIndex++;

        Row headerRow = sheet.createRow(rowIndex++);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("??????");
        headerCell1.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("??????");
        headerCell2.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("??????");
        headerCell3.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell4 = headerRow.createCell(3);
        headerCell4.setCellValue("????????????");
        headerCell4.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell5 = headerRow.createCell(4);
        headerCell5.setCellValue("??????");
        headerCell5.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell6 = headerRow.createCell(5);
        headerCell6.setCellValue("??????");
        headerCell6.setCellStyle(applyTextCellStyle(workbook,"header"));

        if(list.size()>0) {
            // ????????? ???????????? ???????????????
            for (IncomeDto dto : list) {
                Row bodyRow = sheet.createRow(rowIndex++);

                Cell bodyCell1 = bodyRow.createCell(0);
                bodyCell1.setCellValue(Integer.parseInt(dto.getIncomeDate()));
                bodyCell1.setCellStyle(applyTextCellStyle(workbook,"date"));

                Cell bodyCell2 = bodyRow.createCell(1);
                bodyCell2.setCellValue(dto.getIncomeDescription());
                bodyCell2.setCellStyle(applyTextCellStyle(workbook,"")); // ???????????? ??????

                Cell bodyCell3 = bodyRow.createCell(2);
                bodyCell3.setCellValue(dto.getIncomeAmount());
                bodyCell3.setCellStyle(applyNumberCellStyle(workbook,"writePrice"));

                Cell bodyCell4 = bodyRow.createCell(3);
                bodyCell4.setCellValue(dto.getAccountCategoryName());
                bodyCell4.setCellStyle(applyNumberCellStyle(workbook,"writePrice"));

                Cell bodyCell5 = bodyRow.createCell(4);
                bodyCell5.setCellValue(dto.getLargeCategoryName());
                bodyCell5.setCellStyle(applyTextCellStyle(workbook,"center"));

                Cell bodyCell6 = bodyRow.createCell(5);
                bodyCell6.setCellValue(dto.getMemo());
                bodyCell6.setCellStyle(applyTextCellStyle(workbook,"")); // ???????????? ??????
            }
        }else{
            // ????????? ???????????? ?????????
            Row bodyRow = sheet.createRow(rowIndex);
            for (int i=0; i<6; i++) {
                Cell bodyCell = bodyRow.createCell(i);
                if(i==0){bodyCell.setCellValue(excelRequestDto.getPeriod()+" ????????? ???????????? ??????????????? ????????????.");}
                bodyCell.setCellStyle(applyTextCellStyle(workbook, "center"));
            }
            //??? ??????
            sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,0,5));
            rowIndex++;
        }

        // ??????
        Row totalRow = sheet.createRow(rowIndex);
        Cell totalCell1 = totalRow.createCell(0);
        totalCell1.setCellValue("?????? ??????");
        totalCell1.setCellStyle(applyTextCellStyle(workbook,"incomeTotal"));

        Cell totalCell2 = totalRow.createCell(1);
        totalCell2.setCellStyle(applyTextCellStyle(workbook,"incomeTotal"));

        Cell totalCell3 = totalRow.createCell(2);
        totalCell3.setCellStyle(applyNumberCellStyle(workbook,"incomeTotal"));
        totalCell3.setCellFormula("SUM(C5:C" + (5 + list.size() - 1) + ")");
        //int totalCash = list.size()>0? list.stream().mapToInt(IncomeDto::getIncomeAmount).sum() : 0;
        //totalCell3.setCellValue(totalCash);

        Cell totalCell4 = totalRow.createCell(3);
        totalCell4.setCellStyle(applyTextCellStyle(workbook,"incomeTotal"));
        Cell totalCell5 = totalRow.createCell(4);
        totalCell5.setCellStyle(applyTextCellStyle(workbook,"incomeTotal"));
        Cell totalCell6 = totalRow.createCell(5);
        totalCell6.setCellStyle(applyTextCellStyle(workbook,"incomeTotal"));

        //??? ??????
        sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,0,1)); //?????????, ?????????, ?????????, ????????? (??????????????? ?????? 0?????? ??????)
        // ????????? ??????
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        try {
            // ?????? ??????
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ?????? ????????? ?????? ????????????
     * @param response
     * @param excelRequestDto
     */
    @Override
    public void excelDownYearReport(HttpServletResponse response, ExcelRequestDto excelRequestDto) {
        // ?????? ?????? ????????? ????????????
        Workbook workbook = new SXSSFWorkbook();
        // ?????? ?????? ????????? Sheet ??? ?????? ??????????????? (?????? ?????? ???????????? ?????? Sheet??? ?????? ??? ????????????)
        Sheet sheet = workbook.createSheet();
        // ??? ??? ??????
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 3500);
        sheet.setColumnWidth(2, 3500);
        sheet.setColumnWidth(3, 3500);
        sheet.setColumnWidth(4, 3500);
        sheet.setColumnWidth(5, 3500);
        sheet.setColumnWidth(6, 3500);
        sheet.setColumnWidth(7, 3500);
        sheet.setColumnWidth(8, 3500);
        sheet.setColumnWidth(9, 3500);
        sheet.setColumnWidth(10, 3500);
        sheet.setColumnWidth(11, 3500);
        sheet.setColumnWidth(12, 3500);
        sheet.setColumnWidth(13, 3500);

        // ?????? ???????????? ????????? DTO??? ???????????????
        List<YearReportDto> list = reportService.selectYearReport(excelRequestDto.getReportRequestDto());
        // ????????? ???????????????
        int rowIndex = 0;
        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell  = titleRow.createCell(0);
        titleCell.setCellValue("??????????????? ??????");
        titleCell.setCellStyle(applyTextCellStyle(workbook,"title"));

        Row dateRow = sheet.createRow(rowIndex++);
        Cell dateCell  = dateRow.createCell(0);
        dateCell.setCellValue(excelRequestDto.getPeriod());
        dateCell.setCellStyle(applyTextCellStyle(workbook,"title"));
        rowIndex++;

        // ?????????1 header
        Row incomeRow = sheet.createRow(rowIndex++);
        Cell incomeCell1 = incomeRow.createCell(0);
        incomeCell1.setCellValue("?????? ??????");
        incomeCell1.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell incomeCell2 = incomeRow.createCell(1);
        incomeCell2.setCellFormula("SUM(B8:M8)"); // ????????????
        incomeCell2.setCellStyle(applyNumberCellStyle(workbook,"incomePrice"));

        Cell incomeCell3 = incomeRow.createCell(2);
        incomeCell3.setCellStyle(applyTextCellStyle(workbook,""));

        Row expenditureRow = sheet.createRow(rowIndex++);
        Cell expenditureCell1 = expenditureRow.createCell(0);
        expenditureCell1.setCellValue("?????? ??????");
        expenditureCell1.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell expenditureCell2 = expenditureRow.createCell(1);
        expenditureCell2.setCellFormula("SUM(B16:M16)"); // ????????????
        expenditureCell2.setCellStyle(applyNumberCellStyle(workbook,"expenditurePrice"));

        Cell expenditureCell3 = expenditureRow.createCell(2);
        expenditureCell3.setCellStyle(applyTextCellStyle(workbook,""));
        //??? ??????
        int index1 = rowIndex-1;
        int index2 = rowIndex-2;
        sheet.addMergedRegion(new CellRangeAddress(index1,index1,1,2));
        sheet.addMergedRegion(new CellRangeAddress(index2,index2,1,2));
        rowIndex++;

        // ????????? 2 header
        Row headerRow = sheet.createRow(rowIndex++);
        Cell headerFirstCell = headerRow.createCell(0);
        headerFirstCell.setCellValue("??????");
        headerFirstCell.setCellStyle(applyTextCellStyle(workbook,"header"));

        for(int i=0; i<excelRequestDto.getReportRequestDto().getPeriodDtoList().size(); i++){
            PeriodDto dto = excelRequestDto.getReportRequestDto().getPeriodDtoList().get(i);
            Cell headerCell = headerRow.createCell(i+1);
            headerCell.setCellValue(dto.getYear()+"??? "+dto.getMonth()+"???");
            headerCell.setCellStyle(applyTextCellStyle(workbook,"header"));
        }
        Cell headerLastCell = headerRow.createCell(13);
        headerLastCell.setCellValue("??????");
        headerLastCell.setCellStyle(applyTextCellStyle(workbook,"header"));


        for(int i=0; i<list.size(); i++){
            YearReportDto dto = list.get(i);
            Row bodyRow = sheet.createRow(rowIndex++);

            Cell bodyCell1 = bodyRow.createCell(0);
            String value1 = "0".equals(dto.getLargeCategoryId()) || "".equals(dto.getLargeCategoryId())? dto.getLargeCategoryName() : "  ??? " + dto.getLargeCategoryName();
            bodyCell1.setCellValue(value1);

            Cell bodyCell2 = bodyRow.createCell(1);
            Cell bodyCell3 = bodyRow.createCell(2);
            Cell bodyCell4 = bodyRow.createCell(3);
            Cell bodyCell5 = bodyRow.createCell(4);
            Cell bodyCell6 = bodyRow.createCell(5);
            Cell bodyCell7 = bodyRow.createCell(6);
            Cell bodyCell8 = bodyRow.createCell(7);
            Cell bodyCell9 = bodyRow.createCell(8);
            Cell bodyCell10 = bodyRow.createCell(9);
            Cell bodyCell11 = bodyRow.createCell(10);
            Cell bodyCell12 = bodyRow.createCell(11);
            Cell bodyCell13 = bodyRow.createCell(12);
            Cell bodyCell14 = bodyRow.createCell(13);

            if("".equals(dto.getLargeCategoryId())){ // ?????? ??????
                bodyCell1.setCellStyle(applyTextCellStyle(workbook,"yearReportIncome"));
                bodyCell2.setCellFormula("SUM(B9:B15)"); bodyCell2.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
                bodyCell3.setCellFormula("SUM(C9:C15)"); bodyCell3.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
                bodyCell4.setCellFormula("SUM(D9:D15)"); bodyCell4.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
                bodyCell5.setCellFormula("SUM(E9:E15)"); bodyCell5.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
                bodyCell6.setCellFormula("SUM(F9:F15)"); bodyCell6.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
                bodyCell7.setCellFormula("SUM(G9:G15)"); bodyCell7.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
                bodyCell8.setCellFormula("SUM(H9:H15)"); bodyCell8.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
                bodyCell9.setCellFormula("SUM(I9:I15)"); bodyCell9.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
                bodyCell10.setCellFormula("SUM(J9:J15)"); bodyCell10.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
                bodyCell11.setCellFormula("SUM(K9:K15)"); bodyCell11.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
                bodyCell12.setCellFormula("SUM(L9:L15)"); bodyCell12.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
                bodyCell13.setCellFormula("SUM(M9:M15)"); bodyCell13.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
                bodyCell14.setCellFormula("SUM(N9:N15)"); bodyCell14.setCellStyle(applyNumberCellStyle(workbook,"yearReportIncome"));
            }else if("0".equals(dto.getLargeCategoryId())){ // ?????? ??????
                bodyCell1.setCellStyle(applyTextCellStyle(workbook,"yearReportExpenditure"));
                bodyCell2.setCellFormula("SUM(B17:B35)"); bodyCell2.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
                bodyCell3.setCellFormula("SUM(C17:C35)"); bodyCell3.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
                bodyCell4.setCellFormula("SUM(D17:D35)"); bodyCell4.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
                bodyCell5.setCellFormula("SUM(E17:E35)"); bodyCell5.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
                bodyCell6.setCellFormula("SUM(F17:F35)"); bodyCell6.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
                bodyCell7.setCellFormula("SUM(G17:G35)"); bodyCell7.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
                bodyCell8.setCellFormula("SUM(H17:H35)"); bodyCell8.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
                bodyCell9.setCellFormula("SUM(I17:I35)"); bodyCell9.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
                bodyCell10.setCellFormula("SUM(J17:J35)"); bodyCell10.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
                bodyCell11.setCellFormula("SUM(K17:K35)"); bodyCell11.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
                bodyCell12.setCellFormula("SUM(L17:L35)"); bodyCell12.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
                bodyCell13.setCellFormula("SUM(M17:M35)"); bodyCell13.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
                bodyCell14.setCellFormula("SUM(N17:N35)"); bodyCell14.setCellStyle(applyNumberCellStyle(workbook,"yearReportExpenditure"));
            }else{
                bodyCell1.setCellStyle(applyTextCellStyle(workbook,""));
                bodyCell2.setCellValue(dto.getFirstMonth()); bodyCell2.setCellStyle(applyNumberCellStyle(workbook,""));
                bodyCell3.setCellValue(dto.getSecondMonth()); bodyCell3.setCellStyle(applyNumberCellStyle(workbook,""));
                bodyCell4.setCellValue(dto.getThirdMonth()); bodyCell4.setCellStyle(applyNumberCellStyle(workbook,""));
                bodyCell5.setCellValue(dto.getFourthMonth()); bodyCell5.setCellStyle(applyNumberCellStyle(workbook,""));
                bodyCell6.setCellValue(dto.getFifthMonth()); bodyCell6.setCellStyle(applyNumberCellStyle(workbook,""));
                bodyCell7.setCellValue(dto.getSixthMonth()); bodyCell7.setCellStyle(applyNumberCellStyle(workbook,""));
                bodyCell8.setCellValue(dto.getSeventhMonth()); bodyCell8.setCellStyle(applyNumberCellStyle(workbook,""));
                bodyCell9.setCellValue(dto.getEighthMonth()); bodyCell9.setCellStyle(applyNumberCellStyle(workbook,""));
                bodyCell10.setCellValue(dto.getNinthMonth()); bodyCell10.setCellStyle(applyNumberCellStyle(workbook,""));
                bodyCell11.setCellValue(dto.getTenthMonth()); bodyCell11.setCellStyle(applyNumberCellStyle(workbook,""));
                bodyCell12.setCellValue(dto.getEleventhMonth()); bodyCell12.setCellStyle(applyNumberCellStyle(workbook,""));
                bodyCell13.setCellValue(dto.getTwelfthMonth()); bodyCell13.setCellStyle(applyNumberCellStyle(workbook,""));
                bodyCell14.setCellFormula("SUM(B"+(i+8)+":M"+(i+8)+")"); bodyCell14.setCellStyle(applyNumberCellStyle(workbook,"yearReportTotal"));
            }
        }
        // ????????? ??????
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        try {
            // ?????? ??????
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ?????? ?????? ?????? ????????????
     * @param response
     * @param excelRequestDto
     */
    @Override
    public void excelDownBudgetList(HttpServletResponse response, ExcelRequestDto excelRequestDto) {
        // ?????? ?????? ????????? ????????????
        Workbook workbook = new SXSSFWorkbook();
        // ?????? ?????? ????????? Sheet ??? ?????? ??????????????? (?????? ?????? ???????????? ?????? Sheet??? ?????? ??? ????????????)
        Sheet sheet = workbook.createSheet();
        // ??? ??? ??????
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);

        // ?????? ???????????? ????????? DTO??? ???????????????
        BudgetDto budgetDto = budgetService.selectBudgetList(excelRequestDto.getBudgetRequestDto());
        List<BudgetListDto> list = budgetDto.getBudgetListDtoList();

        int rowIndex = 0;
        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell  = titleRow.createCell(0);
        titleCell.setCellValue("?????? ??????");
        titleCell.setCellStyle(applyTextCellStyle(workbook,"title"));

        Row dateRow = sheet.createRow(rowIndex++);
        Cell dateCell  = dateRow.createCell(0);
        dateCell.setCellValue(excelRequestDto.getPeriod());
        dateCell.setCellStyle(applyTextCellStyle(workbook,"title"));
        rowIndex++;

        // ?????????1 header
        Row headerRow1 = sheet.createRow(rowIndex++);
        Cell headerCell1 = headerRow1.createCell(0);
        headerCell1.setCellValue("????????? ??????");
        headerCell1.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell2 = headerRow1.createCell(1);
        headerCell2.setCellValue("????????? ??????");
        headerCell2.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell3 = headerRow1.createCell(2);
        headerCell3.setCellValue("3?????? ??? ????????????");
        headerCell3.setCellStyle(applyTextCellStyle(workbook,"header"));

        Row bodyRow1 = sheet.createRow(rowIndex++);
        Cell bodyCell1 = bodyRow1.createCell(0);
        bodyCell1.setCellValue(Integer.parseInt(budgetDto.getTotalIncome()));
        bodyCell1.setCellStyle(applyNumberCellStyle(workbook, "incomePrice"));

        Cell bodyCell2 = bodyRow1.createCell(1);
        bodyCell2.setCellValue(Integer.parseInt(budgetDto.getLastMonthExpenditure()));
        bodyCell2.setCellStyle(applyNumberCellStyle(workbook, ""));

        Cell bodyCell3 = bodyRow1.createCell(2);
        bodyCell3.setCellValue(Integer.parseInt(budgetDto.getThreeMonthAverageExpenditure()));
        bodyCell3.setCellStyle(applyNumberCellStyle(workbook, ""));
        rowIndex++;

        Row title2Row = sheet.createRow(rowIndex++);
        Cell title2Cell  = title2Row.createCell(0);
        title2Cell.setCellValue("??????????????? ??????");
        title2Cell.setCellStyle(applyTextCellStyle(workbook,"title"));

        // ?????????2 header
        Row headerRow2 = sheet.createRow(rowIndex++);
        Cell headerCell4 = headerRow2.createCell(0);
        headerCell4.setCellValue("??????");
        headerCell4.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell5 = headerRow2.createCell(1);
        headerCell5.setCellValue("??????");
        headerCell5.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell6 = headerRow2.createCell(2);
        headerCell6.setCellValue("??????");
        headerCell6.setCellStyle(applyTextCellStyle(workbook,"header"));

        Cell headerCell7 = headerRow2.createCell(3);
        headerCell7.setCellValue("?????????");
        headerCell7.setCellStyle(applyTextCellStyle(workbook,"header"));

        for(BudgetListDto dto: list){
            Row bodyRow2 = sheet.createRow(rowIndex++);

            Cell bodyCell4 = bodyRow2.createCell(0);
            bodyCell4.setCellValue(dto.getLargeCategoryName());
            bodyCell4.setCellStyle(applyTextCellStyle(workbook,"center"));

            Cell bodyCell5 = bodyRow2.createCell(1);
            bodyCell5.setCellValue(Integer.parseInt(dto.getBudgetAmount()));
            bodyCell5.setCellStyle(applyNumberCellStyle(workbook,""));

            Cell bodyCell6 = bodyRow2.createCell(2);
            bodyCell6.setCellValue(dto.getExpenditureAmount());
            bodyCell6.setCellStyle(applyNumberCellStyle(workbook,""));

            Cell bodyCell7 = bodyRow2.createCell(3);
            bodyCell7.setCellValue(dto.getTotal());
            bodyCell7.setCellStyle(applyNumberCellStyle(workbook,""));
        }


        // ??????
        Row totalRow = sheet.createRow(rowIndex);
        Cell totalCell1 = totalRow.createCell(0);
        totalCell1.setCellValue("??????");
        totalCell1.setCellStyle(applyTextCellStyle(workbook,"budgetTotal"));

        Cell totalCell2 = totalRow.createCell(1);
        totalCell2.setCellStyle(applyNumberCellStyle(workbook,"budgetTotal"));
        totalCell2.setCellFormula("SUM(B9:B" + (9 + list.size() - 1) + ")");

        Cell totalCell3 = totalRow.createCell(2);
        totalCell3.setCellStyle(applyNumberCellStyle(workbook,"budgetTotal"));
        totalCell3.setCellFormula("SUM(C9:C" + (9 + list.size() - 1) + ")");

        Cell totalCell4 = totalRow.createCell(3);
        totalCell4.setCellStyle(applyNumberCellStyle(workbook,"budgetTotal"));
        totalCell4.setCellFormula("SUM(D9:D" + (9 + list.size() - 1) + ")");
        // ????????? ??????
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        try {
            // ?????? ??????
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ?????? ?????? ?????? ?????? ?????? ????????????
     * @param response
     * @param excelRequestDto
     */
    @Override
    public void excelDownBudgetExpenditureList(HttpServletResponse response, ExcelRequestDto excelRequestDto) {
        // ?????? ?????? ????????? ????????????
        Workbook workbook = new SXSSFWorkbook();
        // ?????? ?????? ????????? Sheet ??? ?????? ??????????????? (?????? ?????? ???????????? ?????? Sheet??? ?????? ??? ????????????)
        Sheet sheet = workbook.createSheet();
        // ??? ??? ??????
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 3500);
        sheet.setColumnWidth(2, 3500);
        sheet.setColumnWidth(3, 3500);
        sheet.setColumnWidth(4, 3500);
        sheet.setColumnWidth(5, 3500);
        sheet.setColumnWidth(6, 3500);
        sheet.setColumnWidth(7, 3500);
        sheet.setColumnWidth(8, 3500);
        sheet.setColumnWidth(9, 3500);
        sheet.setColumnWidth(10, 3500);
        sheet.setColumnWidth(11, 3500);
        sheet.setColumnWidth(12, 3500);

        // ?????? ???????????? ????????? DTO??? ???????????????
        List<BudgetExpenditureDto> list = budgetService.selectBudgetExpenditureList(excelRequestDto.getBudgetRequestDto());
        // ????????? ???????????????
        int rowIndex = 0;
        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell  = titleRow.createCell(0);
        titleCell.setCellValue("?????? ?????? ??????");
        titleCell.setCellStyle(applyTextCellStyle(workbook,"title"));

        Row dateRow = sheet.createRow(rowIndex++);
        Cell dateCell  = dateRow.createCell(0);
        dateCell.setCellValue(excelRequestDto.getPeriod());
        dateCell.setCellStyle(applyTextCellStyle(workbook,"title"));
        rowIndex++;

        Row headerRow = sheet.createRow(rowIndex++);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("??????");
        headerCell1.setCellStyle(applyTextCellStyle(workbook,"header"));

        for(int i=0; i<excelRequestDto.getBudgetRequestDto().getPeriodDtoList().size(); i++){
            PeriodDto dto = excelRequestDto.getBudgetRequestDto().getPeriodDtoList().get(i);
            Cell headerCell = headerRow.createCell(i+1);
            headerCell.setCellValue(dto.getYear()+"??? "+dto.getMonth()+"???");
            headerCell.setCellStyle(applyTextCellStyle(workbook,"header"));
        }


        for(int i=0; i<list.size(); i++){
            BudgetExpenditureDto dto = list.get(i);
            Row bodyRow = sheet.createRow(rowIndex++);

            Cell bodyCell1 = bodyRow.createCell(0);
            String value1 = i == 0 ? dto.getLargeCategoryName() : "  ??? " + dto.getLargeCategoryName();
            String styleType1 = i == 0 ? "budgetExpenditureTotal" : "";
            bodyCell1.setCellValue(value1);
            bodyCell1.setCellStyle(applyTextCellStyle(workbook,styleType1));

            Cell bodyCell2 = bodyRow.createCell(1);
            String styleType2 = i == 0 ? "budgetTotal" : "";
            bodyCell2.setCellStyle(applyNumberCellStyle(workbook,styleType2));

            Cell bodyCell3 = bodyRow.createCell(2);
            String styleType3 = i == 0 ? "budgetTotal" : "";
            bodyCell3.setCellStyle(applyNumberCellStyle(workbook,styleType3));

            Cell bodyCell4 = bodyRow.createCell(3);
            String styleType4 = i == 0 ? "budgetTotal" : "";
            bodyCell4.setCellStyle(applyNumberCellStyle(workbook,styleType4));

            Cell bodyCell5 = bodyRow.createCell(4);
            String styleType5 = i == 0 ? "budgetTotal" : "";
            bodyCell5.setCellStyle(applyNumberCellStyle(workbook,styleType5));

            Cell bodyCell6 = bodyRow.createCell(5);
            String styleType6 = i == 0 ? "budgetTotal" : "";
            bodyCell6.setCellStyle(applyNumberCellStyle(workbook,styleType6));

            Cell bodyCell7 = bodyRow.createCell(6);
            String styleType7 = i == 0 ? "budgetTotal" : "";
            bodyCell7.setCellStyle(applyNumberCellStyle(workbook,styleType7));

            Cell bodyCell8 = bodyRow.createCell(7);
            String styleType8 = i == 0 ? "budgetTotal" : "";
            bodyCell8.setCellStyle(applyNumberCellStyle(workbook,styleType8));

            Cell bodyCell9 = bodyRow.createCell(8);
            String styleType9 = i == 0 ? "budgetTotal" : "";
            bodyCell9.setCellStyle(applyNumberCellStyle(workbook,styleType9));

            Cell bodyCell10 = bodyRow.createCell(9);
            String styleType10 = i == 0 ? "budgetTotal" : "";
            bodyCell10.setCellStyle(applyNumberCellStyle(workbook,styleType10));

            Cell bodyCell11 = bodyRow.createCell(10);
            String styleType11 = i == 0 ? "budgetTotal" : "";
            bodyCell11.setCellStyle(applyNumberCellStyle(workbook,styleType11));

            Cell bodyCell12 = bodyRow.createCell(11);
            String styleType12 = i == 0 ? "budgetTotal" : "";
            bodyCell12.setCellStyle(applyNumberCellStyle(workbook,styleType12));

            Cell bodyCell13 = bodyRow.createCell(12);
            String styleType13 = i == 0 ? "budgetTotal" : "";
            bodyCell13.setCellStyle(applyNumberCellStyle(workbook,styleType13));

            if(i==0){
                bodyCell2.setCellFormula("SUM(B6:B" + (6 + list.size() - 2) + ")");
                bodyCell3.setCellFormula("SUM(C6:C" + (6 + list.size() - 2) + ")");
                bodyCell4.setCellFormula("SUM(D6:D" + (6 + list.size() - 2) + ")");
                bodyCell5.setCellFormula("SUM(E6:E" + (6 + list.size() - 2) + ")");
                bodyCell6.setCellFormula("SUM(F6:F" + (6 + list.size() - 2) + ")");
                bodyCell7.setCellFormula("SUM(G6:G" + (6 + list.size() - 2) + ")");
                bodyCell8.setCellFormula("SUM(H6:H" + (6 + list.size() - 2) + ")");
                bodyCell9.setCellFormula("SUM(I6:I" + (6 + list.size() - 2) + ")");
                bodyCell10.setCellFormula("SUM(J6:J" + (6 + list.size() - 2) + ")");
                bodyCell11.setCellFormula("SUM(K6:K" + (6 + list.size() - 2) + ")");
                bodyCell12.setCellFormula("SUM(L6:L" + (6 + list.size() - 2) + ")");
                bodyCell13.setCellFormula("SUM(M6:M" + (6 + list.size() - 2) + ")");
            }else{
                bodyCell2.setCellValue(dto.getFirstMonth());
                bodyCell3.setCellValue(dto.getSecondMonth());
                bodyCell4.setCellValue(dto.getThirdMonth());
                bodyCell5.setCellValue(dto.getFourthMonth());
                bodyCell6.setCellValue(dto.getFifthMonth());
                bodyCell7.setCellValue(dto.getSixthMonth());
                bodyCell8.setCellValue(dto.getSeventhMonth());
                bodyCell9.setCellValue(dto.getEighthMonth());
                bodyCell10.setCellValue(dto.getNinthMonth());
                bodyCell11.setCellValue(dto.getTenthMonth());
                bodyCell12.setCellValue(dto.getEleventhMonth());
                bodyCell13.setCellValue(dto.getTwelfthMonth());
            }
        }
        // ????????? ??????
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        try {
            // ?????? ??????
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * ?????? ?????? ????????????
     * @param response
     * @param pageName
     */
    @Override
    public void excelFormDownload(HttpServletResponse response, String pageName){
        String file_path = "excel_form/"; // ?????????
        String file_name = "Expenditure".equals(pageName)? "expenditure_excel.xlsx" : "income_excel.xlsx"; // ?????????
        try {
            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)))
                    .withRegion(Regions.AP_NORTHEAST_2)
                    .build();

            S3Object s3object = s3.getObject(AWS_BUCKETNAME, file_path+file_name); //#1 - ????????? ???????????????
            InputStream inputStream = s3object.getObjectContent();
            PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
            // ?????? ??????
            Workbook workbook = new XSSFWorkbook(pushbackInputStream);
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * ?????? ?????? ?????? ?????????
     * @param excelRequestDto
     */
    @Transactional
    @Override
    public void excelUploadExpenditureList(ExcelRequestDto excelRequestDto, MultipartFile file) throws Exception {
        List<ExpenditureDto> expenditureDtoList = new ArrayList<>();
        Workbook workbook = null;
        if ("xlsx".equals(excelRequestDto.getFileNameExtension())) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if ("xls".equals(excelRequestDto.getFileNameExtension())) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }
        Sheet worksheet = workbook.getSheetAt(0);
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            Date inputDate = row.getCell(0).getDateCellValue();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
            String expenditureDate = transFormat.format(inputDate);
            String expenditureDescription = row.getCell(1).getStringCellValue();
            Long cash = Double.valueOf(row.getCell(2).getNumericCellValue()).longValue();
            Long card = Double.valueOf(row.getCell(3).getNumericCellValue()).longValue();
            String accountCategoryName = row.getCell(4).getStringCellValue();
            String largeCategoryName = row.getCell(5).getStringCellValue();
            largeCategoryName = largeCategoryName.replace('_', '/');
            String smallCategoryName = row.getCell(6).getStringCellValue();
            smallCategoryName = smallCategoryName.replace('_', '/');
            String memo = row.getCell(7).getStringCellValue();
            ExpenditureDto dto = ExpenditureDto.builder()
                    .expenditureDate(expenditureDate)
                    .expenditureDescription(expenditureDescription)
                    .cash(cash)
                    .card(card)
                    .accountCategoryName(accountCategoryName)
                    .largeCategoryName(largeCategoryName)
                    .smallCategoryName(smallCategoryName)
                    .memo(memo)
                    .userDto(excelRequestDto.getUserDto())
                    .build();

            // ???????????? ID ??????
            ExpenditureDto category = excelMapper.selectExpenditureExcelUploadCategoryId(dto);

            // ?????? ???????????? ID
            dto.setAccountCategoryId(category.getAccountCategoryId());
            // ????????? ???????????? ID
            dto.setLargeCategoryId(category.getLargeCategoryId());
            // ????????? ???????????? ID
            dto.setSmallCategoryId(category.getSmallCategoryId());
            expenditureDtoList.add(dto);
        }
        // ?????? ?????? ??????
        List<Expenditure> expenditureList = expenditureDtoList.stream().map(o -> o.toEntity()).collect(Collectors.toList());
        expenditureRepository.saveAll(expenditureList);
    }
    /**
     * ?????? ?????? ?????? ?????????
     * @param excelRequestDto
     */
    @Transactional
    @Override
    public void excelUploadIncomeList(ExcelRequestDto excelRequestDto, MultipartFile file) throws  Exception {
        List<IncomeDto> incomeDtoList = new ArrayList<>();
        Workbook workbook = null;
        if ("xlsx".equals(excelRequestDto.getFileNameExtension())) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if ("xls".equals(excelRequestDto.getFileNameExtension())) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }
        Sheet worksheet = workbook.getSheetAt(0);
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Row row = worksheet.getRow(i);
            Date inputDate = row.getCell(0).getDateCellValue();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
            String incomeDate = transFormat.format(inputDate);
            String incomeDescription = row.getCell(1).getStringCellValue();
            Long incomeAmount = Double.valueOf( row.getCell(2).getNumericCellValue()).longValue();
            String accountCategoryName = row.getCell(3).getStringCellValue();
            String largeCategoryName = row.getCell(4).getStringCellValue();
            String memo = row.getCell(5).getStringCellValue();
            IncomeDto dto = IncomeDto.builder()
                    .incomeDate(incomeDate)
                    .incomeDescription(incomeDescription)
                    .incomeAmount(incomeAmount)
                    .accountCategoryName(accountCategoryName)
                    .largeCategoryName(largeCategoryName)
                    .memo(memo)
                    .userDto(excelRequestDto.getUserDto())
                    .build();

            // ???????????? ID ??????
            ExpenditureDto category = excelMapper.selectIncomeExcelUploadCategoryId(dto);

            // ?????? ???????????? ID
            dto.setAccountCategoryId(category.getAccountCategoryId());
            // ????????? ???????????? ID
            dto.setLargeCategoryId(category.getLargeCategoryId());
            incomeDtoList.add(dto);
        }
        // ?????? ?????? ??????
        List<Income> incomeList = incomeDtoList.stream()
                .map(o -> o.toEntity())
                .collect(Collectors.toList());
        incomeRepository.saveAll(incomeList);
    }

    /**
     * ????????? ??? ????????? ??????
     * @param workbook
     * @param type
     * @return CellStyle
     */
    private CellStyle applyTextCellStyle(Workbook workbook, String type) {
        XSSFCellStyle cellStyle = (XSSFCellStyle)workbook.createCellStyle();
        XSSFDataFormat format = (XSSFDataFormat)workbook.createDataFormat();
        XSSFFont font = (XSSFFont)workbook.createFont();
        font.setFontName("?????? ??????");
        byte[] rgb = new byte[3];
        if("title".equals(type)){
            // title style
            font.setFontHeight((short)(12*20));
            font.setBold(true); // font bold
            cellStyle.setFont(font);
            return cellStyle;
        }else if("header".equals(type)){
            // header style
            font.setBold(true); // font bold
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //????????? ??????
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //?????? ??????
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("date".equals(type)){
            // ?????? style
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //????????? ??????
            cellStyle.setDataFormat(format.getFormat("####???##???##???"));
        }else if("expenditureTotal".equals(type)){
            // ????????????
            //create the RGB byte array
            rgb[0] = (byte) 198; // red
            rgb[1] = (byte) 224; // green
            rgb[2] = (byte) 180; // blue

            font.setBold(true); // font bold
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //????????? ??????
            cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("incomeTotal".equals(type)) {
            // ????????????
            //create the RGB byte array
            rgb[0] = (byte) 189; // red
            rgb[1] = (byte) 215; // green
            rgb[2] = (byte) 238; // blue

            font.setBold(true); // font bold
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //????????? ??????
            cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("budgetTotal".equals(type)) {
            // ????????????
            font.setBold(true); // font bold
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //????????? ??????
            cellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.index); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("budgetExpenditureTotal".equals(type)) {
            // ?????????????????? ??????
            font.setBold(true); // font bold
            cellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.index); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("yearReportIncome".equals(type)){
            // ??????????????? ???????????? style
            //create the RGB byte array
            rgb[0] = (byte) 221; // red
            rgb[1] = (byte) 235; // green
            rgb[2] = (byte) 247; // blue

            font.setBold(true); // font bold
            cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("yearReportExpenditure".equals(type)){
            // ??????????????? ???????????? style
            //create the RGB byte array
            rgb[0] = (byte) 226; // red
            rgb[1] = (byte) 239; // green
            rgb[2] = (byte) 218; // blue

            font.setBold(true); // font bold
            cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("center".equals(type)) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //????????? ??????
        }
        cellStyle.setFont(font); // font ??????
        // ????????? ??? (???,???,???,??????)
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }
    /**
     * ?????? ??? ????????? ??????
     * @param workbook
     * @param type
     * @return CellStyle
     */
    private CellStyle applyNumberCellStyle(Workbook workbook, String type) {
        XSSFCellStyle cellStyle = (XSSFCellStyle)workbook.createCellStyle();
        XSSFDataFormat format = (XSSFDataFormat)workbook.createDataFormat();
        XSSFFont font = (XSSFFont)workbook.createFont();
        font.setFontName("?????? ??????");
        byte[] rgb = new byte[3];
        if("writePrice".equals(type)){
            // ??????/?????? ?????? style
            cellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.index); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("expenditureTotal".equals(type)){
            // ???????????? ??????
            //create the RGB byte array
            rgb[0] = (byte) 198; // red
            rgb[1] = (byte) 224; // green
            rgb[2] = (byte) 180; // blue

            font.setBold(true); // font bold
            cellStyle.setFont(font);
            cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("incomeTotal".equals(type)){
            // ???????????? ??????
            //create the RGB byte array
            rgb[0] = (byte) 189; // red
            rgb[1] = (byte) 215; // green
            rgb[2] = (byte) 238; // blue

            font.setBold(true); // font bold
            cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("budgetTotal".equals(type)){
            // ???????????? ?????? style
            font.setBold(true); // font bold
            cellStyle.setFont(font);
            cellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.index); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("incomePrice".equals(type)){
            // ?????? ?????? style
            //create the RGB byte array
            rgb[0] = (byte) 68; // red
            rgb[1] = (byte) 114; // green
            rgb[2] = (byte) 196; // blue

            font.setBold(true); // font bold
            font.setColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // ????????? ??????
        }else if("expenditurePrice".equals(type)){
            // ?????? ?????? style
            //create the RGB byte array
            rgb[0] = (byte) 84; // red
            rgb[1] = (byte) 130; // green
            rgb[2] = (byte) 53; // blue

            font.setBold(true); // font bold
            font.setColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // ????????? ??????
        }else if("yearReportIncome".equals(type)){
            // ??????????????? ???????????? ?????? style
            //create the RGB byte array
            rgb[0] = (byte) 221; // red
            rgb[1] = (byte) 235; // green
            rgb[2] = (byte) 247; // blue

            cellStyle.setFont(font);
            cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("yearReportExpenditure".equals(type)){
            // ??????????????? ???????????? ?????? style
            //create the RGB byte array
            rgb[0] = (byte) 226; // red
            rgb[1] = (byte) 239; // green
            rgb[2] = (byte) 218; // blue

            cellStyle.setFont(font);
            cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("yearReportTotal".equals(type)){
            // ??????????????? ?????? ?????? style
            cellStyle.setFont(font);
            cellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.index); // ?????????
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        cellStyle.setFont(font); // font ??????
        cellStyle.setAlignment(HorizontalAlignment.RIGHT); // ????????? ??????
        cellStyle.setDataFormat(format.getFormat("#,##0;[Red]-#,##0"));
        // ????????? ??? (???,???,???,??????)
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }
}
