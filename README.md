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
## SPA(Single Page Application)
SPA(Single Page Application)란 단일 페이지로 구성된 웹 애플리케이션이다.   
페이지 요청 시 새로운 페이지를 불러오는 것이 아니라 하나의 페이지에 필요한 데이터를 서버로부터 전달받아 동적으로 렌더링한다.
<img src="https://user-images.githubusercontent.com/53942595/125581210-a0e0a853-98b4-4c21-9b22-c31929dbd5d1.JPG">
* 장점
  * 페이지 반응성이 빠름
  * 필요한 데이터만 가져와서 렌더링 하기 때문에 효율적임
  * 프론트엔드와 백엔드 분리로 개발업무 분업화 및 협업이 용이함
* 단점
  * 초기 구동 속도가 상대적으로 느림
## JWT(Json Web Token)
JSON 객체로 서버-클라이언트 간에 안전하게 정보를 전송하기 위한 토큰이다.
* ### **JWT 구조**   
  <img src="https://user-images.githubusercontent.com/53942595/125586816-efe6cded-be6e-4fdb-be49-33d95d26c9ae.JPG"><br>
  **1. 헤더(header)**   
&nbsp;&nbsp;헤더는 토큰의 타입과 해싱 알고리즘을 지정하는 정보를 포함한다.   
&nbsp;&nbsp;typ : 토큰의 타입   
&nbsp;&nbsp;alg : 해싱 알고리즘. 해싱 알고리즘으로는 보통 HMAC SHA256 또는 RSA가 사용되며, 토큰 검증시 사용된다.   
  ```
  {
    "alg" : "HS256",
    "typ" : "JWT"
  }
  ```
  **2. 내용(payload)**   
&nbsp;&nbsp;실제 토큰으로 사용하려는 데이터가 담기는 부분. 각 데이터를 Claim이라고 하며 3가지 종류가 있다.   
&nbsp;&nbsp;* **Registered Claim(등록된 클레임)** : 토큰 정보를 표현하기 위해 이미 정해진 데이터 종류이며, 모두 선택적으로 작성 가능하다.   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;iss : 토큰 발급자 (issuer)   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sub : 토큰 제목 (subject)   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;aud : 토큰 대상자 (audience)   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;exp : 토큰의 만료시간 (expiraton), 시간은 NumericDate 형식이어야 함   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;nbf : Not Before 를 의미하며, 토큰의 활성 날짜와 비슷한 개념.   
&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;NumericDate 형식으로 날짜를 지정하며, 이 날짜가 지나기 전까지는 토큰이 처리되지 않음   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;iat : 토큰이 발급된 시간 (issued at), 이 값을 사용하여 토큰 발급 이후 얼마나 시간이 지났는지를 알 수 있음   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;jti : JWT의 고유 식별자로서, 주로 중복적인 처리를 방지하기 위하여 사용되며 일회용 토큰에 사용함   
&nbsp;&nbsp;* **Public Claim(공개 클레임)** : 사용자 정의 Claim. 충돌 방지를 위해 URI 포맷을 이용해 저장   
&nbsp;&nbsp;* **Private Claim(비공개 클레임)** : 사용자 정의 Claim. Public Claim과 다르게 사용자가 임의로 정한 정보   
  ```
  {
    "iss": "test@gmail.com", // 등록된(registered) 클레임
    "iat": 1516239022, // 등록된(registered) 클레임
    "https://householdAccountBook.com": true, // 공개(public) 클레임
    "email": "test@gmail.com", // 비공개(private) 클레임
  }
  ```
  **3. 서명(signature)**   
&nbsp;&nbsp;Header와 Payload의 데이터 무결성과 변조 방지를 위한 서명.   
&nbsp;&nbsp;Header의 인코딩 값과 Payload의 인코딩 값을 합친 후 Secret키와 함께 Header의 해싱 알고리즘으로 인코딩
  ```
  HMACSHA256( 
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret)
  ```
* ### **JWT Process**   
  <img src="https://user-images.githubusercontent.com/53942595/125606374-ff385de0-e459-4f65-9ea0-6d352ab5d0f9.JPG"><br>
  1. 사용자가 ID와 Password를 입력하여 로그인합니다.   
  2. 로그인 성공시 secret key를 통해 JWT 발급합니다.   
  3. client에게 발급된 JWT을 전달합니다.   
  4. 클라이언트에서 Header에 JWT을 담아서 서버에게 데이터를 요청합니다.   
  5. 서버는 JWT Signature를 체크하고 Payload로부터 사용자 정보를 확인해 데이터를 반환합니다.   
  6. 요청에 대한 응답을 클라이언트에게 전달합니다.   
* ### **JWT 저장 방법**
  * HTML5 Web Storage (Local Storage, Session Storage)
    * 클라이언트(브라우저)에 키(key)와 값(value)으로 이루어진 데이터를 저장할 수 있도록 지원하는 HTML5의 새로운 기능이다.
    * 각 도메인과 프로토콜 단위로 저장됨
    * Local Storage : window.localStorage에 위치. 사용자가 직접 데이터를 삭제하기 전까지 영구적으로 보관됨
    * Session Storage : window.sessionStorage에 위치. Local Storage와 달리 데이터가 영구적으로 보관되지 않고 브라우저 창이 닫히면 저장된 데이터가 삭제됨
    * XSS(Cross-Site Scripting) 공격에 취약할 수 있음
    * XSS : 웹 애플리케이션 취약점 중 하나로 웹 페이지에 악성 스크립트를 삽입하여 악의적인 행위를 수행시키는 공격 기법이다.
  * Cookie
    * 사용자가 웹사이트를 방문할 때 사용자의 컴퓨터에 저장되는 작은 데이터 파일
    * 사용자 인증이 유효한 시간을 명시할 수 있으며, 유효 시간이 정해지면 브라우저가 종료되어도 인증이 유지됨
    * 쿠키는 사용자가 따로 요청하지 않아도 브라우저가 Request시에 Request Header를 넣어서 자동으로 서버에 전송함
    * HttpOnly 옵션 : 자바스크립트의 document.cookie를 이용하여 쿠키에 접속하는 것을 막는 옵션 > XSS 공격에 대해 방어 가능
    * Secure 옵션 : 웹 브라우저와 웹 서버가 HTTPS로 통신하는 경우에만 웹 브라우저가 쿠키를 서버로 전송하는 옵션 > 보안 수준을 한 단계 더 높일 수 있음
    * CSRF(Cross-Site Request Forgery) 공격에 취약
    * CSRF : 웹 어플리케이션 취약점 중 하나로 사용자가 자신의 의지와는 무관하게 공격자가 의도한 행위(수정, 삭제, 등록 등)를 특정 웹사이트에 요청하게 하는 공격 기법이다.
* ### **JWT 장점**
  * 사용자 인증에 필요한 모든 정보를 토큰에 포함하기 때문에 별도의 인증 저장소가 필요없음
  * URL 파라미터와 헤더로 사용
  * expired 기능을 내장
* ### **JWT 단점**
  * Payload에 저장하는 정보가 많아질수록 토큰의 길이가 늘어나 네트워크 부하 증가
  * 토큰은 클라이언트에 저장되기 때문에 데이터베이스에서 사용자 정보를 조작하더라도 토큰에 직접 적용할 수 없음
  * Payload는 따로 암호화되지 않아서 디코딩하면 누구나 정보를 확인할 수 있기 때문에 Payload에 중요 데이터를 넣지 않아야 함
