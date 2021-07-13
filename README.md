# :memo:HouseholdAccountBook
Spring Boot(Back-end) + Vue(Front-end)를 이용한 프로젝트로 지출, 수입, 예산을 작성하고 관리하는 가계부 웹 사이트 입니다.
## 프로젝트 구조
<img src="https://user-images.githubusercontent.com/53942595/125481144-1293b4a4-d71f-4633-b258-1120f6c6da11.JPG"><br>
## 개발 환경
### OS
* Windows 10
### Dev Tools
* IntelliJ Community
* Visual Studio Code
### Front-End
* Vue (Vue Theme)
* Javascript
* jQuery
* CSS
* BootstrapVue
### Back-End
* Java 8
* Spring Boot
* MyBatis
* Spring Data JPA
### Data Base
* MariaDB
### Infra
* AWS
## ERD
<img src="https://user-images.githubusercontent.com/53942595/125481261-7f76cbad-10ef-4362-b560-007ffadfe582.JPG"><br>
## 주요기능
### 1. 회원가입 & 로그인
<img src="https://user-images.githubusercontent.com/53942595/125482255-86e08a19-5cae-47f1-8952-7238d56d28a3.png" width="49%" height="49%" >&nbsp;<img src="https://user-images.githubusercontent.com/53942595/125482109-e54deb69-a0f4-480b-a7dc-33bc20dc6431.png" width="49%" height="49%"><br>
* 회원가입과 로그인 각 항목이 빈값이거나 올바른 값이 아니면 항목 밑에 메시지가 표시됩니다.
* 비밀번호 찾기를 하면 등록된 이메일로 임시 비밀번호가 발송됩니다.
* 테스트 계정으로 로그인 가능합니다.
### 2. 쓰기
<img src="https://user-images.githubusercontent.com/53942595/125483170-7fcf3060-014f-47ff-b84b-4a5c50973024.png" width="49%" height="49%" >&nbsp;<img src="https://user-images.githubusercontent.com/53942595/125483164-031ac825-5bf5-490b-9de1-4568972dff26.png" width="49%" height="49%"><br>
* 자바스크립트 기반의 오픈 소스 그리드인 AG Grid를 사용하여 지출/수입 CRUD 기능을 구현하였습니다.
* 마이크로소프트 오피스 파일 포맷을 순수 자바 언어로서 읽고 쓰는 기능을 제공하는 아파치 POI(Apache POI)를 사용하여 엑셀 다운로드/업로드 기능을 구현하였습니다.
* 지출/수입 목록은 최대 6개월까지 조회가 가능합니다.
* 지출 페이지에서 월시작일 변경이 가능합니다.
* 지출 페이지에서 전월의 잔액을 이달의 시작일로 생성하여 정산할 수 있습니다.
### 3. 보고서
<img src="https://user-images.githubusercontent.com/53942595/125483369-fad1a3f9-30d7-4a50-ac1f-00e572f887a6.png" width="49%" height="49%" >&nbsp;<img src="https://user-images.githubusercontent.com/53942595/125483367-8f6885ab-e4bd-4365-876e-afba4f1b1a41.JPG" width="49%" height="49%"><br>
* 월보고서 페이지에서 월단위로 주간지출내역 조회와 Google chart를 사용하여 대분류별 수입/지출 금액과 지난달 일평균, 한달동안 일지출을 차트로 확인할 수 있습니다.
* 연간보고서 페이지에서 연단위로 대분류별 수입/지출 금액과 합계를 조회할 수 있습니다.
* 연간보고서 내역을 엑셀 다운로드할 수 있습니다.
### 4. 예산
<img src="https://user-images.githubusercontent.com/53942595/125483876-fd3801ed-25ba-4ad7-b243-be198197f791.JPG" width="49%" height="49%" >&nbsp;<img src="https://user-images.githubusercontent.com/53942595/125483871-8fe2d2bb-a53d-4e0c-a6a7-c2f5518f7f78.JPG" width="49%" height="49%"><br>
* 월단위로 대분류별 지출 예산 금액을 등록하고, 지출과 남은 금액을 조회할 수 있습니다.
* 연단위로 대분류별 예산-지출 금액을 조회하여 예산이 초과했는지 남았는지 확인할 수 있습니다.
* 예산 목록과 예산 대비 지출 목록을 엑셀 다운로드할 수 있습니다.
