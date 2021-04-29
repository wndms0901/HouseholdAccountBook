package com.app.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.Download;
import com.app.dto.ExcelRequestDto;
import com.app.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/excel")
public class ExcelController {
    private final ExcelService excelService;
    @Value("${spring.aws.bucket.name}")
    private String AWS_BUCKETNAME;
    @Value("${spring.aws.access.key}")
    private String AWS_ACCESS_KEY;
    @Value("${spring.aws.secret.key}")
    private String AWS_SECRET_KEY;

    /**
     * 엑셀 다운로드
     *
     * @param excelRequestDto
     * @return void
     */
    @PostMapping("/download")
    public void excelDownload(HttpServletResponse response
            , @RequestBody ExcelRequestDto excelRequestDto) {
        if ("Expenditure".equals(excelRequestDto.getPageName())) {
            // 지출 목록 엑셀 다운로드
            excelService.excelDownExpenditureList(response, excelRequestDto);
        } else if ("Income".equals(excelRequestDto.getPageName())) {
            // 수입 목록 엑셀 다운로드
            excelService.excelDownIncomeList(response, excelRequestDto);
        } else if ("YearReport".equals(excelRequestDto.getPageName())) {
            // 연간 보고서 엑셀 다운로드
            excelService.excelDownYearReport(response, excelRequestDto);
        } else if ("BudgetWrite".equals(excelRequestDto.getPageName())) {
            // 예산 목록 엑셀 다운로드
            excelService.excelDownBudgetList(response, excelRequestDto);
        } else if ("BudgetExpenditure".equals(excelRequestDto.getPageName())) {
            // 예산 대비 지출 목록 엑셀 다운로드
            excelService.excelDownBudgetExpenditureList(response, excelRequestDto);
        }
    }

    /**
     * 엑셀 양식 다운로드
     *
     * @param pageName
     * @return void
     */
    @GetMapping("/form/download")
    public void excelFormDownload(HttpServletResponse response
            , @RequestParam String pageName) {
        Regions clientRegion = Regions.DEFAULT_REGION;
        S3Object headerOverrideObject = null;
        String file_path = "excel_form/"; // 폴더명
        String file_name = "expenditure_excel.xlsx"; // 파일명
        try {
            AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)))
                    .withRegion(Regions.AP_NORTHEAST_2)
                    .build();

            //String destFilename = "/Users/ykoh/Desktop/test.png";
            S3Object s3object = s3.getObject(AWS_BUCKETNAME, file_path+file_name); //#1 - 파일을 다운로드함
            InputStream inputStream = s3object.getObjectContent();
            PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
            // 엑셀 출력
            Workbook workbook = new XSSFWorkbook(pushbackInputStream);
            workbook.write(response.getOutputStream());
            workbook.close();
//            S3ObjectInputStream inputStream = s3object.getObjectContent();
//            FileUtils.copyInputStreamToFile(inputStream, new File(destFilename));  //#2 - 스트림을 파일로 저장함
           //s3object.close();
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

 }
