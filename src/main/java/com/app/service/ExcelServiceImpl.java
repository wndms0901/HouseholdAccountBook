package com.app.service;

import com.app.dto.*;
import com.app.mapper.WriteMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ExcelServiceImpl implements ExcelService{
    private final WriteMapper writeMapper;

    /**
     * 지출 목록 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     * @return void
     */
    @Override
    public void excelDownExpenditureList(HttpServletResponse response, ExcelRequestDto excelRequestDto) {
        // 엑셀 파일 하나를 만듭니다
        Workbook workbook = new SXSSFWorkbook();
        // 엑셀 파일 내부에 Sheet 를 하나 생성합니다 (엑셀 파일 하나에는 여러 Sheet가 있을 수 있습니다)
        Sheet sheet = workbook.createSheet();
        // 열 폭 수정
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 7000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 7000);

        // 엑셀 렌더링에 필요한 DTO를 가져옵니다
        List<ExpenditureDto> list = writeMapper.selectExpenditureList(excelRequestDto.getWriteRequestDto());

        // 헤더를 생성합니다
        int rowIndex = 0;
        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell  = titleRow.createCell(0);
        titleCell.setCellValue("지출 현황");
        titleCell.setCellStyle(applyCellStyle(workbook,"title"));

        Row dateRow = sheet.createRow(rowIndex++);
        Cell dateCell  = dateRow.createCell(0);
        dateCell.setCellValue(excelRequestDto.getPeriod());
        dateCell.setCellStyle(applyCellStyle(workbook,"title"));
        rowIndex++;

        Row headerRow = sheet.createRow(rowIndex++);
        //headerRow.setRowStyle(applyCellStyle(workbook,"header"));
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("날짜");
        headerCell1.setCellStyle(applyCellStyle(workbook,"header"));

        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("사용내역");
        headerCell2.setCellStyle(applyCellStyle(workbook,"header"));

        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("현금");
        headerCell3.setCellStyle(applyCellStyle(workbook,"header"));

        Cell headerCell4 = headerRow.createCell(3);
        headerCell4.setCellValue("카드");
        headerCell4.setCellStyle(applyCellStyle(workbook,"header"));

        Cell headerCell5 = headerRow.createCell(4);
        headerCell5.setCellValue("출금통장");
        headerCell5.setCellStyle(applyCellStyle(workbook,"header"));

        Cell headerCell6 = headerRow.createCell(5);
        headerCell6.setCellValue("대분류");
        headerCell6.setCellStyle(applyCellStyle(workbook,"header"));

        Cell headerCell7 = headerRow.createCell(6);
        headerCell7.setCellValue("소분류");
        headerCell7.setCellStyle(applyCellStyle(workbook,"header"));

        Cell headerCell8 = headerRow.createCell(7);
        headerCell8.setCellValue("메모");
        headerCell8.setCellStyle(applyCellStyle(workbook,"header"));

       // 바디에 데이터를 넣어줍니다
        for (ExpenditureDto dto : list) {
            Row bodyRow = sheet.createRow(rowIndex++);

            Cell bodyCell1 = bodyRow.createCell(0);
            bodyCell1.setCellValue(Integer.parseInt(dto.getExpenditureDate()));
            bodyCell1.setCellStyle(applyCellStyle(workbook,"date"));

            Cell bodyCell2 = bodyRow.createCell(1);
            bodyCell2.setCellValue(dto.getExpenditureDescription());
            bodyCell2.setCellStyle(applyCellStyle(workbook,"")); // 테두리만 적용

            Cell bodyCell3 = bodyRow.createCell(2);
            bodyCell3.setCellValue(dto.getCash());
            bodyCell3.setCellStyle(applyCellStyle(workbook,"price"));

            Cell bodyCell4 = bodyRow.createCell(3);
            bodyCell4.setCellValue(dto.getCard());
            bodyCell4.setCellStyle(applyCellStyle(workbook,"price"));

            Cell bodyCell5 = bodyRow.createCell(4);
            bodyCell5.setCellValue(dto.getAccountCategoryName());
            bodyCell5.setCellStyle(applyCellStyle(workbook,"price"));

            Cell bodyCell6 = bodyRow.createCell(5);
            bodyCell6.setCellValue(dto.getLargeCategoryName());
            bodyCell6.setCellStyle(applyCellStyle(workbook,"center"));

            Cell bodyCell7 = bodyRow.createCell(6);
            bodyCell7.setCellValue(dto.getSmallCategoryName());
            bodyCell7.setCellStyle(applyCellStyle(workbook,"center"));

            Cell bodyCell8 = bodyRow.createCell(7);
            bodyCell8.setCellValue(dto.getMemo());
            bodyCell8.setCellStyle(applyCellStyle(workbook,"")); // 테두리만 적용
        }
        // 합계
        Row totalRow = sheet.createRow(rowIndex);
        Cell totalCell1 = totalRow.createCell(0);
        totalCell1.setCellValue("지출 합계");
        totalCell1.setCellStyle(applyCellStyle(workbook,"expenditureTotal"));

        Cell totalCell2 = totalRow.createCell(1);
        totalCell2.setCellStyle(applyCellStyle(workbook,"expenditureTotal"));

        Cell totalCell3 = totalRow.createCell(2);
        totalCell3.setCellStyle(applyCellStyle(workbook,"expenditureTotalPrice"));
        int totalCash = list.stream().mapToInt(ExpenditureDto::getCash).sum();
        totalCell3.setCellValue(totalCash);

        Cell totalCell4 = totalRow.createCell(3);
        totalCell4.setCellStyle(applyCellStyle(workbook,"expenditureTotalPrice"));
        int totalCard = list.stream().mapToInt(ExpenditureDto::getCard).sum();
        totalCell4.setCellValue(totalCard);

        Cell totalCell5 = totalRow.createCell(4);
        totalCell5.setCellStyle(applyCellStyle(workbook,"expenditureTotal"));
        Cell totalCell6 = totalRow.createCell(5);
        totalCell6.setCellStyle(applyCellStyle(workbook,"expenditureTotal"));
        Cell totalCell7 = totalRow.createCell(6);
        totalCell7.setCellStyle(applyCellStyle(workbook,"expenditureTotal"));
        Cell totalCell8 = totalRow.createCell(7);
        totalCell8.setCellStyle(applyCellStyle(workbook,"expenditureTotal"));

        //셀 병합
        sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,0,1)); //열시작, 열종료, 행시작, 행종료 (자바배열과 같이 0부터 시작)
        try {
            // 엑셀 출력
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 수입 목록 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     * @return void
     */
    @Override
    public void excelDownIncomeList(HttpServletResponse response, ExcelRequestDto excelRequestDto) {
        // 엑셀 파일 하나를 만듭니다
        Workbook workbook = new SXSSFWorkbook();
        // 엑셀 파일 내부에 Sheet 를 하나 생성합니다 (엑셀 파일 하나에는 여러 Sheet가 있을 수 있습니다)
        Sheet sheet = workbook.createSheet();
        // 열 폭 수정
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 7000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 7000);

        // 엑셀 렌더링에 필요한 DTO를 가져옵니다
        List<IncomeDto> list = writeMapper.selectIncomeList(excelRequestDto.getWriteRequestDto());

        // 헤더를 생성합니다
        int rowIndex = 0;
        Row titleRow = sheet.createRow(rowIndex++);
        Cell titleCell  = titleRow.createCell(0);
        titleCell.setCellValue("수입 현황");
        titleCell.setCellStyle(applyCellStyle(workbook,"title"));

        Row dateRow = sheet.createRow(rowIndex++);
        Cell dateCell  = dateRow.createCell(0);
        dateCell.setCellValue(excelRequestDto.getPeriod());
        dateCell.setCellStyle(applyCellStyle(workbook,"title"));
        rowIndex++;

        Row headerRow = sheet.createRow(rowIndex++);
        //headerRow.setRowStyle(applyCellStyle(workbook,"header"));
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("날짜");
        headerCell1.setCellStyle(applyCellStyle(workbook,"header"));

        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("내역");
        headerCell2.setCellStyle(applyCellStyle(workbook,"header"));

        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("금액");
        headerCell3.setCellStyle(applyCellStyle(workbook,"header"));

        Cell headerCell4 = headerRow.createCell(3);
        headerCell4.setCellValue("입금통장");
        headerCell4.setCellStyle(applyCellStyle(workbook,"header"));

        Cell headerCell5 = headerRow.createCell(4);
        headerCell5.setCellValue("분류");
        headerCell5.setCellStyle(applyCellStyle(workbook,"header"));

        Cell headerCell6 = headerRow.createCell(5);
        headerCell6.setCellValue("메모");
        headerCell6.setCellStyle(applyCellStyle(workbook,"header"));

        // 바디에 데이터를 넣어줍니다
        for (IncomeDto dto : list) {
            Row bodyRow = sheet.createRow(rowIndex++);

            Cell bodyCell1 = bodyRow.createCell(0);
            bodyCell1.setCellValue(Integer.parseInt(dto.getIncomeDate()));
            bodyCell1.setCellStyle(applyCellStyle(workbook,"date"));

            Cell bodyCell2 = bodyRow.createCell(1);
            bodyCell2.setCellValue(dto.getIncomeDescription());
            bodyCell2.setCellStyle(applyCellStyle(workbook,"")); // 테두리만 적용

            Cell bodyCell3 = bodyRow.createCell(2);
            bodyCell3.setCellValue(dto.getIncomeAmount());
            bodyCell3.setCellStyle(applyCellStyle(workbook,"price"));

            Cell bodyCell4 = bodyRow.createCell(3);
            bodyCell4.setCellValue(dto.getAccountCategoryName());
            bodyCell4.setCellStyle(applyCellStyle(workbook,"price"));

            Cell bodyCell5 = bodyRow.createCell(4);
            bodyCell5.setCellValue(dto.getLargeCategoryName());
            bodyCell5.setCellStyle(applyCellStyle(workbook,"center"));

            Cell bodyCell6 = bodyRow.createCell(5);
            bodyCell6.setCellValue(dto.getMemo());
            bodyCell6.setCellStyle(applyCellStyle(workbook,"")); // 테두리만 적용
        }
        // 합계
        Row totalRow = sheet.createRow(rowIndex);
        Cell totalCell1 = totalRow.createCell(0);
        totalCell1.setCellValue("수입 합계");
        totalCell1.setCellStyle(applyCellStyle(workbook,"incomeTotal"));

        Cell totalCell2 = totalRow.createCell(1);
        totalCell2.setCellStyle(applyCellStyle(workbook,"incomeTotal"));

        Cell totalCell3 = totalRow.createCell(2);
        totalCell3.setCellStyle(applyCellStyle(workbook,"incomeTotalPrice"));
        int totalCash = list.stream().mapToInt(IncomeDto::getIncomeAmount).sum();
        totalCell3.setCellValue(totalCash);

        Cell totalCell4 = totalRow.createCell(3);
        totalCell4.setCellStyle(applyCellStyle(workbook,"incomeTotal"));
        Cell totalCell5 = totalRow.createCell(4);
        totalCell5.setCellStyle(applyCellStyle(workbook,"incomeTotal"));
        Cell totalCell6 = totalRow.createCell(5);
        totalCell6.setCellStyle(applyCellStyle(workbook,"incomeTotal"));

        //셀 병합
        sheet.addMergedRegion(new CellRangeAddress(rowIndex,rowIndex,0,1)); //열시작, 열종료, 행시작, 행종료 (자바배열과 같이 0부터 시작)
        try {
            // 엑셀 출력
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 연간 보고서 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     * @return void
     */
    @Override
    public void excelDownYearReport(HttpServletResponse response, ExcelRequestDto excelRequestDto) {

    }

    /**
     * 예산 목록 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     * @return void
     */
    @Override
    public void excelDownBudgetList(HttpServletResponse response, ExcelRequestDto excelRequestDto) {

    }

    /**
     * 예산 대비 지출 목록 엑셀 다운로드
     * @param response
     * @param excelRequestDto
     * @return void
     */
    @Override
    public void excelDownBudgetExpenditureList(HttpServletResponse response, ExcelRequestDto excelRequestDto) {

    }
    /**
     * 셀 스타일 적용
     * @param workbook
     * @param type
     * @return CellStyle
     */
    private CellStyle applyCellStyle(Workbook workbook, String type) {
        XSSFCellStyle cellStyle = (XSSFCellStyle)workbook.createCellStyle();
        XSSFDataFormat format = (XSSFDataFormat)workbook.createDataFormat();
        Font font = workbook.createFont();
        font.setFontName("맑은 고딕");
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
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); //중앙 정렬
            cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index); // 배경색
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("date".equals(type)){
            // 날짜 style
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
            cellStyle.setDataFormat(format.getFormat("####년##월##일"));
        }else if("price".equals(type)){
            // 금액 style
            cellStyle.setAlignment(HorizontalAlignment.RIGHT); // 오른쪽 정렬
            cellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.index); // 배경색
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setDataFormat(format.getFormat("#,##0"));
        }else if("expenditureTotal".equals(type)){
            // 지출합계
            //create the RGB byte array
            rgb[0] = (byte) 198; // red
            rgb[1] = (byte) 224; // green
            rgb[2] = (byte) 180; // blue

            font.setBold(true); // font bold
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
            cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // 배경색
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("expenditureTotalPrice".equals(type)){
            // 지출합계 금액
            //create the RGB byte array
            rgb[0] = (byte) 198; // red
            rgb[1] = (byte) 224; // green
            rgb[2] = (byte) 180; // blue

            font.setBold(true); // font bold
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.RIGHT); // 오른쪽 정렬
            cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // 배경색
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        }else if("incomeTotal".equals(type)) {
            // 수입합계
            //create the RGB byte array
            rgb[0] = (byte) 189; // red
            rgb[1] = (byte) 215; // green
            rgb[2] = (byte) 238; // blue

            font.setBold(true); // font bold
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
            cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // 배경색
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }else if("incomeTotalPrice".equals(type)){
            // 수입합계 금액
            //create the RGB byte array
            rgb[0] = (byte) 189; // red
            rgb[1] = (byte) 215; // green
            rgb[2] = (byte) 238; // blue

            font.setBold(true); // font bold
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.RIGHT); // 오른쪽 정렬
            cellStyle.setFillForegroundColor(new XSSFColor(rgb, new DefaultIndexedColorMap())); // 배경색
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        }else if("center".equals(type)) {
            cellStyle.setAlignment(HorizontalAlignment.CENTER); //가운데 정렬
        }
        // 테두리 선 (우,좌,위,아래)
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }
}
