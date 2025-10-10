# CMS - 콘텐츠 관리 시스템

## 개요
직원 근태 관리 및 사용자 관리에 중점을 둔 Spring Boot 기반의 콘텐츠 관리 시스템(CMS)입니다. 이 시스템은 근태 추적, 휴가 신청 관리 및 사용자 인증을 위한 웹 기반 인터페이스를 제공합니다.

## 기술 스택

### 백엔드 프레임워크
- **Spring Boot 3.2.5** - 메인 애플리케이션 프레임워크
- **Java 17** - 프로그래밍 언어 (Amazon Corretto 17.0.10)
- **Gradle 8.7** - 빌드 도구 및 의존성 관리

### Spring Framework 구성 요소
- **Spring Security 6** - JWT를 사용한 인증 및 권한 부여
- **Spring Data JPA** - Hibernate를 사용한 데이터베이스 추상화 계층
- **Spring Web MVC** - 웹 계층 및 REST API
- **Spring WebFlux** - 반응형 프로그래밍 지원
- **MyBatis 3.0.3** - SQL 매핑 프레임워크 (JPA와 함께 사용)
- **Thymeleaf** - 서버 사이드 템플릿 엔진
- **Spring Session** - 세션 관리

### 데이터베이스 및 영속성
- **MySQL/MariaDB** - 주 데이터베이스 (MariaDB dialect)
- **JPA/Hibernate** - DDL 자동 업데이트가 활성화된 ORM
- **MyBatis** - 사용자 정의 SQL 쿼리 및 복잡한 작업

### 보안 및 인증
- **JWT (JSON Web Tokens)** - 토큰 기반 인증
- **BCrypt** - 비밀번호 암호화
- **Spring Security** - 종합적인 보안 프레임워크

### 복원력 및 모니터링
- **Resilience4j** - 서킷 브레이커, 재시도 및 격벽 패턴
- **Spring Boot Actuator** - 애플리케이션 모니터링 및 헬스 체크

### 추가 라이브러리
- **Lombok** - 보일러플레이트 코드를 줄이기 위한 코드 생성
- **ModelMapper** - 객체 매핑 유틸리티
- **Thymeleaf Layout Dialect** - 템플릿 레이아웃 관리

## 사용된 오픈 소스 라이브러리

### 핵심 Spring Framework
- **Spring Boot 3.2.5** - 자동 구성이 포함된 메인 애플리케이션 프레임워크
- **Spring Data JPA** - 리포지토리 패턴을 사용한 데이터 액세스 계층
- **Spring Data JDBC** - JDBC 기반 데이터 액세스
- **Spring Security 6** - 인증 및 권한 부여 프레임워크
- **Spring Web MVC** - REST API 및 컨트롤러를 위한 웹 프레임워크
- **Spring WebFlux** - 반응형 웹 프레임워크
- **Spring Web Services** - SOAP 웹 서비스 지원
- **Spring Session Core** - 세션 관리
- **Spring Cloud Dependencies 2023.0.1** - 클라우드 네이티브 패턴

### 데이터베이스 및 영속성
- **MySQL Connector/J** - MySQL 데이터베이스 드라이버
- **MyBatis Spring Boot Starter 3.0.3** - SQL 매핑 프레임워크 통합
- **Hibernate/JPA** - 객체-관계 매핑 (Spring Data JPA에 포함)

### 보안 및 인증
- **JJWT 0.12.3** - 토큰 생성 및 검증을 위한 JWT 라이브러리
  - `jjwt-api` - JWT API
  - `jjwt-impl` - JWT 구현
  - `jjwt-jackson` - JSON 처리를 위한 Jackson 통합
- **Thymeleaf Extras Spring Security 6** - Spring Security와 Thymeleaf 통합

### 복원력 및 서킷 브레이커
- **Resilience4j** - 장애 허용 라이브러리
  - `spring-cloud-starter-circuitbreaker-resilience4j` - 블로킹 호출용 서킷 브레이커
  - `spring-cloud-starter-circuitbreaker-reactor-resilience4j` - 반응형 호출용 서킷 브레이커

### 템플릿 엔진 및 프론트엔드
- **Thymeleaf** - 서버 사이드 템플릿 엔진 (Spring Boot starter에 포함)
- **Thymeleaf Layout Dialect** - Thymeleaf 템플릿을 위한 레이아웃 관리
- **AG-Grid Community** - 표 형식 데이터를 표시하기 위한 고급 데이터 그리드 컴포넌트
  - 위치: `/src/main/resources/static/js/aggrid/ag-grid-community.min.js`
  - CSS 테마: `/src/main/resources/static/css/aggrid/ag-theme-alpine.css`
- **Flatpickr** - 한국어 로케일을 지원하는 경량 날짜 선택기 라이브러리
  - CDN: `https://cdn.jsdelivr.net/npm/flatpickr`
  - 한국어 로케일: `https://cdn.jsdelivr.net/npm/flatpickr/dist/l10n/ko.js`
- **jQuery 3.6.0** - DOM 조작 및 AJAX를 위한 JavaScript 라이브러리
- **SweetAlert2** - 아름답고 반응형인 팝업 박스
- **Lodash** - JavaScript 유틸리티 라이브러리
- **FullCalendar** - 전체 크기 캘린더 컴포넌트
- **Bootstrap 5.3.3** - 반응형 디자인을 위한 CSS 프레임워크

### 유틸리티 및 도구
- **Lombok** - 보일러플레이트 코드를 줄이기 위한 어노테이션 기반 코드 생성
- **ModelMapper 3.2.0** - 객체 간 매핑 유틸리티
- **Spring Boot DevTools** - 개발 시간 도구 (핫 리로드 등)
- **Spring Boot Configuration Processor** - 구성 메타데이터 생성

### 로깅
- **Logback Classic** - 로깅 구현
- **SLF4J API** - Simple Logging Facade for Java

### 테스트 프레임워크
- **Spring Boot Starter Test** - 다음을 포함하는 종합 테스트 스타터:
  - JUnit 5 (Jupiter)
  - Mockito
  - AssertJ
  - Hamcrest
  - Spring Test & Spring Boot Test
- **Reactor Test** - 반응형 스트림을 위한 테스트 유틸리티
- **Spring Security Test** - 보안 테스트 유틸리티
- **JUnit Platform Launcher** - 테스트 런타임

### 빌드 및 개발 도구
- **Gradle 8.7** - 빌드 자동화 도구
- **Spring Dependency Management Plugin 1.1.7** - 의존성 버전 관리

## 프로젝트 구조

```
src/
├── main/
│   ├── java/io/github/mskim/comm/cms/
│   │   ├── CmsApplication.java           # 메인 Spring Boot 애플리케이션
│   │   ├── api/                          # 외부 API 통합
│   │   ├── config/                       # 구성 클래스
│   │   │   ├── SecurityConfig.java       # Spring Security 구성
│   │   │   ├── CorsProperties.java       # CORS 구성
│   │   │   ├── ModelMapperConfig.java    # ModelMapper 구성
│   │   │   └── ThymeleafConfig.java      # Thymeleaf 구성
│   │   ├── controller/                   # 웹 컨트롤러
│   │   │   ├── api/                      # REST API 컨트롤러
│   │   │   └── view/                     # 뷰 컨트롤러 (Thymeleaf)
│   │   ├── dto/                          # 데이터 전송 객체
│   │   ├── entity/                       # JPA 엔티티
│   │   ├── jwt/                          # JWT 유틸리티 및 필터
│   │   ├── mapper/                       # MyBatis 매퍼
│   │   ├── repository/                   # JPA 리포지토리
│   │   ├── service/                      # 서비스 인터페이스
│   │   ├── serviceImpl/                  # 서비스 구현
│   │   └── sp/                           # 저장 프로시저 관련
│   └── resources/
│       ├── application.yml               # 메인 구성
│       ├── mapper/                       # MyBatis XML 매퍼
│       ├── static/                       # 정적 웹 리소스
│       │   ├── css/                      # 스타일시트 (Bootstrap 5.3.3)
│       │   ├── js/                       # JavaScript 파일
│       │   ├── img/                      # 이미지
│       │   ├── fonts/                    # 웹 폰트
│       │   └── svg/                      # SVG 아이콘
│       └── templates/                    # Thymeleaf 템플릿
│           ├── attendance/               # 근태 관리 뷰
│           ├── fragments/                # 재사용 가능한 템플릿 조각
│           ├── layout/                   # 레이아웃 템플릿
│           └── main/                     # 메인 애플리케이션 뷰
└── test/                                 # 테스트 소스
```

## 핵심 엔티티 및 도메인 모델

### 사용자 관리
- **Users** - 로그인 자격 증명 및 프로필이 있는 핵심 사용자 엔티티
- **UserLoginHistory** - 사용자 로그인 활동 추적

### 근태 관리
- **UserAttendance** - 일일 근태 기록 (출근/퇴근)
- **UserAttendanceChangeRequest** - 근태 수정 요청
- **UserLeaveRequest** - 휴가/연차 요청

## 주요 기능

### 인증 및 보안
- JWT 기반 상태 비저장 인증
- 쿠키 기반 토큰 저장 (HttpOnly, Secure)
- 역할 기반 접근 제어
- 다중 도메인을 위한 CORS 구성
- 사용자 정의 인증 진입점

### 근태 시스템
- 일일 출퇴근 추적
- 8시간 근무일 계산
- 월별 근무일 수 집계
- 승인 워크플로우가 있는 근태 수정 요청
- 휴가 요청 관리 (연차, 외근, 출장)
- 다중 조건을 사용한 고급 검색 기능
- 페이지네이션 및 정렬 기능이 있는 AG-Grid 기반 목록 뷰

### 복원력 패턴
- 외부 서비스 호출을 위한 서킷 브레이커
- 구성 가능한 시도 횟수를 가진 재시도 메커니즘
- 요청 격리를 위한 격벽 패턴
- 동시 요청을 위한 스레드 풀 관리

## 사용 가능한 Gradle 작업

### 개발 작업
```bash
./gradlew bootRun              # Spring Boot 애플리케이션 실행
./gradlew bootTestRun          # 테스트 런타임 클래스패스로 실행
./gradlew build                # 프로젝트 빌드
./gradlew test                 # 테스트 실행
./gradlew bootJar              # 실행 가능한 JAR 생성
./gradlew bootBuildImage       # OCI/Docker 이미지 빌드
```

### 개발 워크플로우
```bash
# 개발 서버 시작
./gradlew bootRun

# 프로덕션용 빌드
./gradlew build

# 테스트 실행
./gradlew test
```

## 구성

### 데이터베이스 구성
- **Database**: localhost:3306의 MySQL/MariaDB
- **Schema**: cms
- **Connection**: Asia/Seoul 타임존을 사용한 UTF-8 인코딩
- **JPA**: 개발을 위해 DDL 자동 업데이트 활성화

### 서버 구성
- **Port**: 8080
- **Session Timeout**: 30분
- **Error Handling**: 사용자 정의 오류 페이지 활성화

### 보안 구성
- **JWT Secret**: application.yml에 구성됨
- **CORS Origins**: localhost, 127.0.0.1 및 특정 IP 주소
- **Session**: 상태 비저장 (JWT 기반)

## 개발 환경 설정

### 사전 요구 사항
- JDK 17 (Amazon Corretto 권장)
- MySQL/MariaDB 서버
- Gradle 8.7 (또는 래퍼 사용)
- IntelliJ IDEA 2021.2.1+ (권장)

### IDE 구성
- UTF-8 파일 인코딩 활성화
- 빌드 도구를 Gradle IntelliJ IDEA로 설정
- Async Stack Traces 비활성화
- Launch Optimization 비활성화

### 데이터베이스 설정
1. MySQL/MariaDB 설치
2. 'cms'라는 이름의 데이터베이스 생성
3. 필요한 경우 application.yml의 자격 증명 업데이트
4. JPA가 첫 실행 시 테이블을 자동으로 생성

## 아키텍처 패턴

### 계층화된 아키텍처
- **Controller Layer**: HTTP 요청 처리 (뷰 + API 컨트롤러)
- **Service Layer**: 비즈니스 로직 구현
- **Repository Layer**: 데이터 액세스 (JPA + MyBatis)
- **Entity Layer**: 도메인 모델 및 데이터베이스 엔티티

### 보안 아키텍처
- 상태 비저장 인증을 위한 JWT 필터 체인
- 사용자 정의 인증 진입점
- 역할 기반 접근 제어
- 크로스 오리진 요청을 위한 CORS 처리

### 복원력 아키텍처
- 외부 종속성을 위한 서킷 브레이커 패턴
- 일시적인 실패를 위한 재시도 메커니즘
- 리소스 격리를 위한 격벽 패턴
- Actuator를 통한 헬스 체크 엔드포인트

## 이해해야 할 주요 파일

### 구성 파일
- `/src/main/resources/application.yml` - 메인 애플리케이션 구성
- `/src/main/java/io/github/mskim/comm/cms/config/SecurityConfig.java` - 보안 설정
- `/build.gradle` - 프로젝트 의존성 및 빌드 구성

### 핵심 애플리케이션 파일
- `/src/main/java/io/github/mskim/comm/cms/CmsApplication.java` - 애플리케이션 진입점
- `/src/main/java/io/github/mskim/comm/cms/controller/view/MainViewController.java` - 메인 페이지 컨트롤러
- `/src/main/java/io/github/mskim/comm/cms/jwt/` - JWT 인증 컴포넌트

### 프론트엔드 리소스
- `/src/main/resources/templates/` - Thymeleaf 템플릿
- `/src/main/resources/static/` - CSS, JS, 이미지 및 기타 정적 자산

## 개발 노트

### 코드 스타일 및 관례
- 보일러플레이트 코드를 줄이기 위해 Lombok 사용
- 필드 주입보다 생성자 주입 선호
- 서비스 계층은 인터페이스 + 구현 패턴 사용
- 복잡한 SQL 쿼리를 위한 MyBatis XML 매퍼
- 서버 사이드 렌더링을 위한 Thymeleaf

### 테스트
- 테스트를 위한 JUnit 5 플랫폼
- 통합 테스트를 위한 Spring Boot Test
- 반응형 컴포넌트를 위한 Reactor Test

### 모니터링 및 관찰 가능성
- Spring Boot Actuator 엔드포인트 활성화
- 서킷 브레이커 헬스 인디케이터
- 사용자 정의 오류 컨트롤러를 사용한 상세한 오류 처리

이 코드베이스는 보안, 복원력 및 모니터링과 같은 엔터프라이즈급 기능에 중점을 둔 Spring Boot 모범 사례를 따릅니다. JPA와 MyBatis를 모두 사용하는 하이브리드 접근 방식은 단순한 CRUD 작업과 복잡한 쿼리 모두에 유연성을 제공합니다.

## 최근 보안 개선 사항 (2025-10-03)

### 수정된 중요한 보안 문제
다음 보안 취약점이 식별되고 해결되었습니다:

1. **JWT 토큰 만료 버그 수정** (`JWTUtil.java:37`)
   - **문제**: 토큰 만료 계산이 100을 곱하여 토큰이 의도한 것보다 훨씬 늦게 만료됨
   - **수정**: 올바른 밀리초 값을 사용하기 위해 `* 100` 곱셈 제거
   - **영향**: 토큰이 이제 의도한 시간에 만료되어 무단 연장 액세스 방지

2. **환경 변수 외부화** (`application.yml`)
   - **문제**: 구성 파일에 하드코딩된 데이터베이스 자격 증명 및 JWT 비밀
   - **수정**: 민감한 값을 환경 변수로 외부화
   - **변수**: `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `DDL_AUTO`
   - **추가됨**: 환경 설정 안내를 위한 `.env.example` 파일

3. **쿠키 보안 구현 수정** (`JWTFilter.java:89`)
   - **문제**: 쿠키에 잘못된 토큰 사용 (새 토큰 대신 이전 토큰)
   - **수정**: 쿠키 저장소에서 새 토큰을 사용하도록 업데이트
   - **개선**: 요청 프로토콜에 따라 조건부로 보안 쿠키 설정

4. **포괄적인 입력 유효성 검사 추가**
   - **Controllers**: 모든 API 컨트롤러에 `@Validated` 및 `@Valid` 어노테이션 추가
   - **DTOs**: 모든 데이터 전송 객체에 유효성 검사 제약 조건 추가
   - **Global Exception Handler**: 중앙 집중식 유효성 검사 오류 처리 생성
   - **기능**:
     - 강력한 비밀번호 정책 (8자 이상, 대소문자 혼합, 숫자, 특수 문자)
     - 로그인 ID 형식 유효성 검사 (영숫자만)
     - 날짜 형식 유효성 검사 (yyyy-MM-dd 패턴)
     - 필드 길이 및 패턴 유효성 검사
     - 더 나은 UX를 위한 한국어 오류 메시지

### 구현된 보안 모범 사례

#### 비밀번호 보안
- 최소 8자 필요
- 대문자, 소문자, 숫자 및 특수 문자 포함 필수
- 로그인 ID는 영숫자 문자만 허용

#### 입력 정제
- 모든 API 엔드포인트가 이제 입력 매개변수 유효성 검사
- 상세한 오류 메시지가 있는 요청 본문 유효성 검사
- 매개변수 형식 유효성 검사 (날짜, ID 등)

#### 환경 구성
- 모든 민감한 구성 외부화
- 프로덕션 준비된 환경 변수 설정
- 다양한 환경에 대한 별도 구성

### 환경 설정
이 애플리케이션을 안전하게 실행하려면 다음 환경 변수를 설정하세요:

```bash
# 데이터베이스 구성
export DB_USERNAME=your_database_username
export DB_PASSWORD=your_secure_database_password

# JWT 구성
export JWT_SECRET=your_strong_jwt_secret_key

# 애플리케이션 구성
export DDL_AUTO=update  # 프로덕션에는 'validate' 사용
```

### 보안 유효성 검사 명령
환경 변수를 설정한 후 애플리케이션이 올바르게 시작되는지 확인하세요:
```bash
# 애플리케이션 시작
./gradlew bootRun

# 보안 엔드포인트 확인
curl -X POST http://localhost:8080/joinProc \
  -H "Content-Type: application/json" \
  -d '{"loginId":"test","password":"short"}'
# 유효성 검사 오류가 반환되어야 함

# 근태 API 유효성 검사 테스트
curl "http://localhost:8080/api/attendance/month/all?startDate=invalid&endDate=2024-12-31"
# 날짜 형식 유효성 검사 오류가 반환되어야 함
```

### 보안을 위해 수정된 파일
- `/src/main/java/io/github/mskim/comm/cms/jwt/JWTUtil.java` - 토큰 만료 수정
- `/src/main/java/io/github/mskim/comm/cms/jwt/JWTFilter.java` - 쿠키 구현 수정
- `/src/main/resources/application.yml` - 외부화된 구성
- `/src/main/java/io/github/mskim/comm/cms/controller/api/AttendanceApiController.java` - 유효성 검사 추가
- `/src/main/java/io/github/mskim/comm/cms/controller/api/JoinApiController.java` - 유효성 검사 추가
- `/src/main/java/io/github/mskim/comm/cms/dto/JoinDTO.java` - 유효성 검사 제약 조건 추가
- `/src/main/java/io/github/mskim/comm/cms/dto/UserAttendanceChangeRequestDTO.java` - 유효성 검사 제약 조건 추가
- `/src/main/java/io/github/mskim/comm/cms/exception/GlobalExceptionHandler.java` - 오류 처리를 위한 새 파일
- `/.env.example` - 환경 변수 안내를 위한 새 파일

### 중요한 보안 참고 사항
- **프로덕션 배포**: 프로덕션 환경에서는 항상 `DDL_AUTO=validate` 사용
- **HTTPS 필요**: 보안 쿠키 설정은 프로덕션에서 HTTPS와 함께 제대로 작동
- **비밀 관리**: 프로덕션에서는 적절한 비밀 관리 시스템(예: HashiCorp Vault) 사용
- **정기 업데이트**: 의존성을 업데이트하고 보안 취약점 모니터링

이 코드베이스는 이제 보안 모범 사례를 따르며 적절한 입력 유효성 검사, 안전한 구성 관리 및 강력한 오류 처리를 통해 프로덕션 준비가 완료되었습니다.

## 최근 기능 업데이트 (2025-10-08)

### 검색 기능 구현
근태 관리 모듈 전반에 걸쳐 포괄적인 검색 시스템이 구현되어 유연하고 사용자 친화적인 데이터 필터링 기능을 제공합니다.

#### 프론트엔드 개선 사항

##### 1. 공통 JavaScript 함수 (`/src/main/resources/static/js/apps.js`)
공통 패턴을 재사용 가능한 함수로 추출 및 중앙 집중화:

**AG-Grid 함수:**
- `initAgGrid(gridId, columnDefs, options)` - 페이지네이션이 있는 표준 그리드 초기화
- `initAgGridSearchBar(searchBarId, gridApi)` - 그리드 필터링을 위한 빠른 검색 바
- `statusCellRenderer(params)` - 배지 스타일링이 있는 공통 상태 셀 렌더러

**FullCalendar 함수:**
- `initFullCalendar(calendarId, options)` - 캘린더 초기화
- `compareDateWithToday(dateStr, compareDate)` - 날짜 비교 유틸리티

**AJAX 및 폼 함수:**
- `handleAjaxError(xhr, defaultMessage)` - 중앙 집중식 오류 처리
- `postRequest(url, data, successCallback, errorCallback)` - 간소화된 POST 요청
- `validateRequiredFields(validations)` - 공통 유효성 검사 로직

**검색 폼 함수:**
- `initDatePicker(elementId, options)` - Flatpickr 날짜 선택기 초기화
- `initDateRangePicker(startElementId, endElementId)` - 유효성 검사가 있는 날짜 범위 선택기
- `serializeSearchForm(formId)` - 폼 입력을 쿼리 매개변수로 변환
- `resetSearchForm(formId, callback)` - 폼 및 날짜 선택기 초기화
- `createSearchForm(containerId, searchFields, onSearch, onReset)` - 동적 검색 폼 생성기

##### 2. AG-Grid ID 표준화
일반 그리드 ID를 목적별 이름으로 변경:
- `att03.html`: `myGrid` → `leaveRequestGrid`
- `att04.html`: `myGrid` → `attendanceRequestGrid`

일관성을 위해 모든 그리드 높이를 **600px**로 표준화했습니다.

##### 3. 고급 검색 폼

**휴가 요청 검색** (`att03.html` / `att03.js`):
- 검색 조건: 신청상태, 신청자, 신청유형, 기간, 신청날짜 범위
- 총 6개의 검색 필드로 동적 필터링
- "조회" 및 "초기화" 버튼으로 실시간 검색

**근태 조정 요청 검색** (`att04.html` / `att04.js`):
- 검색 조건: 신청상태, 신청자, 신청일시 범위, 근무일 범위
- 날짜 범위 유효성 검사가 있는 6개의 검색 필드
- 휴가 요청 검색과 일관된 UI/UX

##### 4. 날짜 선택기 통합
- **라이브러리**: 한국어 로케일이 있는 Flatpickr
- **형식**: YYYY-MM-DD
- **기능**:
  - 날짜 범위 유효성 검사 (시작 날짜 ≤ 종료 날짜)
  - 쉬운 재설정을 위한 지우기 버튼
  - 키보드 입력 지원
  - 모바일 친화적 인터페이스

#### 백엔드 개선 사항

##### 1. 검색 매개변수 클래스 (`sp/` 패키지)

**UserLeaveRequestSP.java:**
```java
- RequestStatus status           // 신청상태 필터
- String userName                 // 신청자명 검색
- LeaveType leaveType            // 연차/외근/출장 필터
- PeriodType periodType          // 종일/오전/오후 필터
- LocalDate requestDateStart     // 신청날짜 시작
- LocalDate requestDateEnd       // 신청날짜 종료
- LocalDate createdAtStart       // 생성일시 시작
- LocalDate createdAtEnd         // 생성일시 종료
```

**UserAttendanceChangeRequestSP.java:**
```java
// MyBatis Mapper 필드
- String userId                  // 사용자 ID (Mapper용)
- LocalDate workDate             // 근무일 (Mapper용)

// 검색 필드
- ChangeStatus status            // 신청상태 필터
- String userName                // 신청자명 검색
- LocalDate workDateStart        // 근무일 시작
- LocalDate workDateEnd          // 근무일 종료
- LocalDate createdAtStart       // 생성일시 시작
- LocalDate createdAtEnd         // 생성일시 종료
```

##### 2. 리포지토리 검색 쿼리

**SpEL을 사용한 JPQL 동적 필터링:**
```java
@Query("SELECT lr FROM UserLeaveRequest lr " +
       "WHERE (:#{#sp.status} IS NULL OR lr.status = :#{#sp.status}) " +
       "AND (:#{#sp.userName} IS NULL OR lr.user.name LIKE %:#{#sp.userName}%) " +
       "AND (:#{#sp.leaveType} IS NULL OR lr.leaveType = :#{#sp.leaveType}) " +
       "AND (:#{#sp.periodType} IS NULL OR lr.periodType = :#{#sp.periodType}) " +
       "AND (:#{#sp.requestDateStart} IS NULL OR lr.requestDate >= :#{#sp.requestDateStart}) " +
       "AND (:#{#sp.requestDateEnd} IS NULL OR lr.requestDate <= :#{#sp.requestDateEnd}) " +
       "AND (:#{#sp.createdAtStart} IS NULL OR DATE(lr.createdAt) >= :#{#sp.createdAtStart}) " +
       "AND (:#{#sp.createdAtEnd} IS NULL OR DATE(lr.createdAt) <= :#{#sp.createdAtEnd}) " +
       "ORDER BY lr.createdAt DESC")
List<UserLeaveRequest> searchLeaveRequests(@Param("sp") UserLeaveRequestSP sp);
```

**주요 기능:**
- 모든 검색 매개변수는 선택 사항 (NULL 안전)
- 포함 경계를 사용한 날짜 범위 필터링
- LIKE를 사용한 사용자 이름 부분 일치
- 상태, 휴가 유형, 기간 유형에 대한 Enum 기반 필터링
- 생성 날짜별로 결과 정렬 (최신순)

##### 3. 서비스 계층

**UserLeaveRequestService.java:**
```java
List<UserLeaveRequestResponseDTO> searchLeaveRequests(UserLeaveRequestSP sp);
```

**UserAttendanceChangeRequestService.java:**
```java
List<UserAttendanceChangeRequestResponseDTO> searchAttendanceChangeRequests(UserAttendanceChangeRequestSP sp);
```

두 서비스 모두 다음을 처리합니다:
- 사용자 및 승인자 엔티티를 위한 지연 로딩 초기화
- `from()` 팩토리 메서드를 사용한 DTO 변환
- `@Transactional(readOnly = true)`를 사용한 트랜잭션 경계 관리

##### 4. REST API 엔드포인트

**LeaveApiController.java:**
```java
@GetMapping("/request/search")
public List<UserLeaveRequestResponseDTO> searchLeaveRequests(
        @RequestParam(required = false) UserLeaveRequest.RequestStatus status,
        @RequestParam(required = false) String userName,
        @RequestParam(required = false) UserLeaveRequest.LeaveType leaveType,
        @RequestParam(required = false) UserLeaveRequest.PeriodType periodType,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDateStart,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDateEnd,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtStart,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtEnd
)
```

**AttendanceApiController.java:**
```java
@GetMapping("/request/search")
public List<UserAttendanceChangeRequestResponseDTO> searchAttendanceChangeRequests(
        @RequestParam(required = false) UserAttendanceChangeRequest.ChangeStatus status,
        @RequestParam(required = false) String userName,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workDateStart,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate workDateEnd,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtStart,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtEnd
)
```

**API 엔드포인트:**
- `GET /api/v1/leave/request/search` - 휴가 요청 검색
- `GET /api/v1/attendance/request/search` - 근태 조정 요청 검색

**쿼리 매개변수:** 자동 날짜 파싱이 있는 모든 선택 사항

#### 코드 품질 개선

##### 1. JavaScript 리팩토링
- **이전**: 6개 파일에 걸쳐 약 200줄의 중복 코드
- **이후**: `apps.js`에 약 100줄의 공통 함수
- **감소**: 유지 관리성이 향상된 약 50% 코드 감소

##### 2. 일관된 패턴
- 모든 검색 폼이 동일한 구조 및 스타일 사용
- 모든 그리드가 일관된 구성 사용 (페이지네이션, 정렬, 필터링)
- 모든 API 호출이 표준 오류 처리 사용
- 모든 날짜 입력이 한국어 로케일이 있는 Flatpickr 사용

##### 3. 검색 매개변수 패턴
SP (검색 매개변수) 패턴은 관심사를 분리합니다:
- **MyBatis 전용 필드**: 기존 매퍼 쿼리에서 사용 (userId, workDate)
- **검색 전용 필드**: 새로운 JPA 리포지토리 쿼리에서 사용 (userName, 날짜 범위, enums)
- **주석**: 필드 목적에 대한 명확한 문서화

#### 수정/생성된 파일

**프론트엔드:**
- `/src/main/resources/static/js/apps.js` - 공통 함수 추가
- `/src/main/resources/static/js/domain/attendance/att01.js` - 공통 함수를 사용하도록 리팩토링
- `/src/main/resources/static/js/domain/attendance/att01-m01.js` - 공통 함수를 사용하도록 리팩토링
- `/src/main/resources/static/js/domain/attendance/att02.js` - 공통 함수를 사용하도록 리팩토링
- `/src/main/resources/static/js/domain/attendance/att02-m01.js` - 공통 함수를 사용하도록 리팩토링
- `/src/main/resources/static/js/domain/attendance/att03.js` - 검색 폼으로 완전히 재작성
- `/src/main/resources/static/js/domain/attendance/att04.js` - 검색 폼으로 완전히 재작성
- `/src/main/resources/templates/attendance/att03.html` - 검색 컨테이너 추가
- `/src/main/resources/templates/attendance/att04.html` - 검색 컨테이너 추가
- `/src/main/resources/templates/layout/default.html` - Flatpickr CDN 링크 추가

**백엔드:**
- `/src/main/java/io/github/mskim/comm/cms/sp/UserLeaveRequestSP.java` - 새 검색 매개변수 클래스
- `/src/main/java/io/github/mskim/comm/cms/sp/UserAttendanceChangeRequestSP.java` - 검색 필드로 업데이트
- `/src/main/java/io/github/mskim/comm/cms/repository/UserLeaveRequestRepository.java` - 검색 쿼리 추가
- `/src/main/java/io/github/mskim/comm/cms/repository/UserAttendanceChangeRequestRepository.java` - 검색 쿼리 추가
- `/src/main/java/io/github/mskim/comm/cms/service/UserLeaveRequestService.java` - 검색 메서드 추가
- `/src/main/java/io/github/mskim/comm/cms/service/UserAttendanceChangeRequestService.java` - 검색 메서드 추가
- `/src/main/java/io/github/mskim/comm/cms/serviceImpl/UserLeaveRequestServiceImpl.java` - 검색 구현
- `/src/main/java/io/github/mskim/comm/cms/serviceImpl/UserAttendanceChangeRequestServiceImpl.java` - 검색 구현
- `/src/main/java/io/github/mskim/comm/cms/controller/api/LeaveApiController.java` - 검색 엔드포인트 추가
- `/src/main/java/io/github/mskim/comm/cms/controller/api/AttendanceApiController.java` - 검색 엔드포인트 추가

#### 기술적 하이라이트

1. **동적 쿼리 빌드**: SpEL 표현식이 MyBatis XML 없이 NULL 안전 동적 쿼리를 가능하게 함
2. **코드 재사용성**: 공통 함수 라이브러리가 중복을 줄이고 일관성을 보장함
3. **사용자 경험**: 한국어 로케일, 직관적인 날짜 선택기, 실시간 필터링
4. **관심사 분리**: SP 패턴이 MyBatis 및 JPA 사용 사례를 명확하게 분리
5. **타입 안전성**: 상태, 휴가 유형 및 기간 유형에 대한 Enum 기반 필터링
6. **성능**: 적절한 ORDER BY 및 날짜 범위 필터링을 사용한 인덱싱된 쿼리

#### 향후 개선 사항
- Excel로 내보내기 기능
- 저장된 검색 필터 (사용자 환경 설정)
- 고급 날짜 단축키 (이번 주, 지난달 등)
- 상태 및 유형에 대한 다중 선택 필터링
- 검색 기록 및 최근 검색

이 검색 기능은 향후 데이터 관리 기능을 위한 견고한 기반을 제공하며 Spring Boot 및 바닐라 JavaScript를 사용한 현대적인 풀스택 개발 패턴을 보여줍니다.

## CMS 명명 규칙 (Naming Conventions)

이 프로젝트는 일관된 코드베이스 유지를 위해 엄격한 명명 규칙을 따릅니다. 자세한 내용은 [NAMING_CONVENTIONS.md](NAMING_CONVENTIONS.md) 파일을 참조하세요.

### 주요 명명 규칙 요약

#### Java 클래스 명명
- **Entity**: `{도메인명}` (예: `Users`, `UserAttendance`, `UserLeaveRequest`)
- **DTO**: `{도메인명}DTO` 또는 `{도메인명}{용도}DTO` (예: `UserLeaveRequestResponseDTO`)
- **Repository**: `{도메인명}Repository` (예: `UserAttendanceRepository`)
- **Service**: `{도메인명}Service` / `{도메인명}ServiceImpl`
- **Controller**:
  - API: `{도메인명}ApiController` (예: `AttendanceApiController`)
  - View: `{도메인명}ViewController` (예: `LeaveViewController`)
- **Search Parameters**: `{도메인명}SP` (예: `UserLeaveRequestSP`)

#### 파일 명명
- **HTML 메인 페이지**: `{약어}{번호}.html` (예: `att01.html`, `att02.html`)
- **HTML 모달**: `{약어}{번호}-m{모달번호}.html` (예: `att01-m01.html`)
- **JavaScript 메인**: `{약어}{번호}.js` (예: `att01.js`, `att02.js`)
- **JavaScript 모달**: `{약어}{번호}-m{모달번호}.js` (예: `att02-m01.js`)

#### URL/URI 패턴
- **View URL**: `/view/{도메인}{번호}` (예: `/view/attendance01`, `/view/attendance02`)
- **API URI**: `/api/v1/{도메인}/{동작}` (예: `/api/v1/attendance/check-in`, `/api/v1/leave/request`)

#### 메서드/함수 명명
- **Java 메서드**: camelCase with prefixes
  - `find`: 조회 (예: `findByLoginId()`)
  - `create`: 생성 (예: `createLeaveRequest()`)
  - `update`: 수정
  - `delete`: 삭제
  - `check`: 확인/검증 (예: `checkIn()`, `checkOut()`)
- **JavaScript 함수**: camelCase with prefixes
  - `init`: 초기화 (예: `initCalendar()`)
  - `onClick`: 클릭 이벤트 (예: `onClickLeaveRequestBtn()`)
  - `get`/`set`: 데이터 접근

#### 데이터베이스 명명
- **테이블명**: UPPER_SNAKE_CASE (예: `USER_ATTENDANCE`, `USER_LEAVE_REQUEST`)
- **컬럼명**: lower_snake_case (예: `login_id`, `request_date`, `created_at`)
- **Enum 값**: UPPER_SNAKE_CASE (예: `ANNUAL_LEAVE`, `ALL_DAY`, `REQUEST`)

#### 공통 약어
| 약어 | 전체명 | 설명 |
|------|------|------|
| att | Attendance | 근태, 출퇴근 관련 |
| lv | Leave | 휴가, 외출 관련 |
| usr | User | 사용자 관련 |
| hist | History | 이력 관련 |
| DTO | Data Transfer Object | 데이터 전송 객체 |
| SP | Search Parameters | 검색 매개변수 |

### 페이지 번호 규칙
| 번호 | 의미 | 예시 |
|------|------|------|
| 01 | 조회/수정 페이지 | att01 (근태 수정 요청) |
| 02 | 등록 페이지 | att02 (휴가/외출 신청) |
| 03 | 목록 조회 페이지 | att03 (휴가/외출 신청 조회) |
| 04 | 통계 페이지 | att04 (근무 일수 통계) |

### 명명 규칙 적용 예시

휴가/외출 신청 기능을 구현할 때:

```
Backend (Java):
├── Entity: UserLeaveRequest.java
├── DTO: UserLeaveRequestDTO.java, UserLeaveRequestResponseDTO.java
├── Repository: UserLeaveRequestRepository.java
├── Service: UserLeaveRequestService.java, UserLeaveRequestServiceImpl.java
├── Controller: LeaveApiController.java, LeaveViewController.java
└── SP: UserLeaveRequestSP.java

Frontend (HTML/JS):
├── View: att02.html (휴가/외출 신청)
├── View: att03.html (휴가/외출 신청 조회)
├── Modal: att02-m01.html (신청 모달)
├── Script: att02.js (신청 메인 스크립트)
├── Script: att02-m01.js (신청 모달 스크립트)
└── Script: att03.js (조회 메인 스크립트)

API Endpoints:
├── POST /api/v1/leave/request (휴가/외출 신청)
└── GET /api/v1/leave/request/all (모든 신청 조회)

View URLs:
├── GET /view/attendance02 (휴가/외출 신청 페이지)
└── GET /view/attendance03 (휴가/외출 신청 조회 페이지)

Database:
└── Table: USER_LEAVE_REQUEST
    ├── id (기본키)
    ├── user_id (사용자 ID)
    ├── leave_type (휴가 타입: ANNUAL_LEAVE, FIELD_WORK, BUSINESS_TRIP)
    ├── request_date (신청 날짜)
    ├── period_type (기간 타입: ALL_DAY, MORNING, AFTERNOON)
    ├── status (상태: REQUEST, APPROVE, REJECT)
    ├── created_at (생성일시)
    └── updated_at (수정일시)
```

### 명명 규칙 준수 체크리스트

코드 작성 후 다음 사항을 확인하세요:

- [ ] Entity 클래스명이 도메인명을 명확하게 표현하는가?
- [ ] DTO 클래스명에 DTO 접미사가 올바르게 붙었는가?
- [ ] Repository, Service, Controller 명명이 규칙을 따르는가?
- [ ] HTML/JavaScript 파일명이 약어+번호 형식인가?
- [ ] API URI가 `/api/v1/{도메인}/{동작}` 형식인가?
- [ ] View URL이 `/view/{도메인}{번호}` 형식인가?
- [ ] 메서드명이 camelCase이며 적절한 접두어가 붙었는가?
- [ ] 상수명이 UPPER_SNAKE_CASE인가?
- [ ] 데이터베이스 테이블/컬럼 명명이 규칙을 따르는가?

이러한 명명 규칙은 코드의 가독성과 유지보수성을 향상시키며, 팀 전체가 일관된 코드 스타일을 유지하는 데 도움이 됩니다. 자세한 내용과 추가 예시는 [NAMING_CONVENTIONS.md](NAMING_CONVENTIONS.md) 파일을 참조하세요.
