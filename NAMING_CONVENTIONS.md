# CMS 명명 규칙 (Naming Conventions)

## 목차
1. [Java 클래스 명명 규칙](#java-클래스-명명-규칙)
2. [HTML/Thymeleaf 파일 명명 규칙](#htmlthymeleaf-파일-명명-규칙)
3. [JavaScript 파일 명명 규칙](#javascript-파일-명명-규칙)
4. [URL/URI 규칙](#urluri-규칙)
5. [메서드/함수 명명](#메서드함수-명명)
6. [데이터베이스 테이블/컬럼 명명 규칙](#데이터베이스-테이블컬럼-명명-규칙)
7. [공통 약어 정리](#공통-약어-정리)

---

## Java 클래스 명명 규칙

### 1. Entity 클래스
- **규칙**: `{도메인명}` (복수형)
- **예시**:
  - `Users` - 사용자 엔티티
  - `UserAttendance` - 사용자 근태 기록
  - `UserAttendanceChangeRequest` - 근태 수정 요청
  - `UserLeaveRequest` - 휴가/외출/출장 요청

### 2. DTO (Data Transfer Object) 클래스
- **규칙**: `{도메인명}DTO` 또는 `{도메인명}{용도}DTO`
- **예시**:
  - `UserDTO` - 사용자 기본 DTO
  - `UserAttendanceDTO` - 근태 기록 DTO
  - `UserAttendanceChangeRequestDTO` - 근태 수정 요청 입력 DTO
  - `UserAttendanceChangeRequestResponseDTO` - 근태 수정 요청 응답 DTO
  - `UserLeaveRequestDTO` - 휴가/외출 요청 입력 DTO
  - `UserLeaveRequestResponseDTO` - 휴가/외출 요청 응답 DTO

### 3. Repository 인터페이스
- **규칙**: `{도메인명}Repository`
- **예시**:
  - `UserRepository`
  - `UserAttendanceRepository`
  - `UserAttendanceChangeRequestRepository`
  - `UserLeaveRequestRepository`

### 4. Service 인터페이스 및 구현체
- **규칙**:
  - 인터페이스: `{도메인명}Service`
  - 구현체: `{도메인명}ServiceImpl`
- **예시**:
  - `UserService` / `UserServiceImpl`
  - `UserAttendanceService` / `UserAttendanceServiceImpl`
  - `UserAttendanceChangeRequestService` / `UserAttendanceChangeRequestServiceImpl`
  - `UserLeaveRequestService` / `UserLeaveRequestServiceImpl`

### 5. Controller 클래스
- **규칙**:
  - API 컨트롤러: `{도메인명}ApiController`
  - View 컨트롤러: `{도메인명}ViewController`
- **예시**:
  - `JoinApiController` - 회원가입 API
  - `AttendanceApiController` - 근태 관련 API
  - `LeaveApiController` - 휴가/외출 API
  - `MainViewController` - 메인 페이지 컨트롤러
  - `LeaveViewController` - 휴가/외출 페이지 컨트롤러

### 6. Mapper 인터페이스 (MyBatis)
- **규칙**: `{도메인명}Mapper`
- **예시**:
  - `UserAttendanceChangeRequestMapper`

### 7. Search Parameters (SP) 클래스
- **규칙**: `{도메인명}SP`
- **예시**:
  - `UserAttendanceSP`
  - `UserAttendanceChangeRequestSP`

---

## HTML/Thymeleaf 파일 명명 규칙

### 1. 메인 페이지 파일
- **규칙**: `{약어}{번호}.html`
- **예시**:
  - `att01.html` - 근태 수정 요청 (Attendance 01)
  - `att02.html` - 휴가/외출 신청 (Attendance 02)
  - `att03.html` - 휴가/외출 신청 조회 (Attendance 03)
  - `att04.html` - 근무 일수 통계 (Attendance 04)

### 2. 모달 파일
- **규칙**: `{약어}{번호}-m{모달번호}.html`
- **예시**:
  - `att01-m01.html` - 근태 수정 요청 모달
  - `att02-m01.html` - 휴가/외출 신청 모달

### 3. Fragment 파일
- **규칙**: `{이름}.html`
- **예시**:
  - `header.html` - 헤더 프래그먼트
  - `footer.html` - 푸터 프래그먼트

---

## JavaScript 파일 명명 규칙

### 1. 메인 페이지용 JS 파일
- **규칙**: `{약어}{번호}.js`
- **예시**:
  - `att01.js` - 근태 수정 요청 메인 스크립트
  - `att02.js` - 휴가/외출 신청 메인 스크립트
  - `att03.js` - 휴가/외출 신청 조회 메인 스크립트
  - `att04.js` - 근무 일수 통계 메인 스크립트

### 2. 모달용 JS 파일
- **규칙**: `{약어}{번호}-m{모달번호}.js`
- **예시**:
  - `att01-m01.js` - 근태 수정 요청 모달 스크립트
  - `att02-m01.js` - 휴가/외출 신청 모달 스크립트

---

## URL/URI 규칙

### 1. View URL (사용자에게 보이는 페이지)
- **규칙**: `/view/{도메인}{번호}`
- **예시**:
  - `/view/attendance01` - 근태 수정 요청 페이지
  - `/view/attendance02` - 휴가/외출 신청 페이지
  - `/view/attendance03` - 휴가/외출 신청 조회 페이지
  - `/view/attendance04` - 근무 일수 통계 페이지

### 2. API URI (REST API 엔드포인트)
- **규칙**: `/api/v1/{도메인}/{동작}`
- **예시**:
  - `/api/v1/attendance/check-in` - 출근 등록
  - `/api/v1/attendance/check-out` - 퇴근 등록
  - `/api/v1/attendance/change/request` - 근태 수정 요청
  - `/api/v1/attendance/request/all` - 모든 근태 수정 요청 조회
  - `/api/v1/leave/request` - 휴가/외출 신청
  - `/api/v1/leave/request/all` - 모든 휴가/외출 신청 조회

### 3. API 경로 상수
- **규칙**: `ApiPaths` 클래스에 정의
- **예시**:
  ```java
  public static final String BASE_API = "/api/v1";
  public static final String ATTENDANCE = BASE_API + "/attendance";
  public static final String LEAVE = BASE_API + "/leave";
  ```

---

## 메서드/함수 명명

### 1. Java 메서드
- **규칙**: camelCase
- **접두어 규칙**:
  - `find`: 조회 (단건 또는 목록)
  - `create`: 생성
  - `update`: 수정
  - `delete`: 삭제
  - `check`: 확인/검증
  - `validate`: 유효성 검증
  - `count`: 개수 세기
- **예시**:
  - `findByLoginId(String loginId)` - 로그인 ID로 사용자 조회
  - `findAllUserAttendanceThisMonth()` - 이번 달 모든 사용자 근태 조회
  - `createLeaveRequest()` - 휴가 요청 생성
  - `checkIn()` - 출근 등록
  - `checkOut()` - 퇴근 등록

### 2. JavaScript 함수
- **규칙**: camelCase
- **접두어 규칙**:
  - `init`: 초기화
  - `onClick`: 클릭 이벤트 핸들러
  - `onChange`: 변경 이벤트 핸들러
  - `on`: 일반 이벤트 핸들러
  - `get`: 데이터 가져오기
  - `set`: 데이터 설정하기
- **예시**:
  - `initCalendar()` - 캘린더 초기화
  - `initLeaveRequestModal()` - 휴가 신청 모달 초기화
  - `onClickLeaveRequestBtn()` - 휴가 신청 버튼 클릭 핸들러
  - `initLeaveRequestGrid()` - 휴가 신청 AG-Grid 초기화

### 3. 변수명
- **규칙**: camelCase (Java, JavaScript 모두)
- **예시**:
  - Java: `loginId`, `userName`, `requestDate`, `leaveType`
  - JavaScript: `selectedDate`, `calendar`, `leaveRequestGrid`, `apiUri`

### 4. 상수명
- **규칙**: UPPER_SNAKE_CASE
- **예시**:
  - Java: `BASE_API`, `ATTENDANCE`, `LEAVE`
  - JavaScript: `const API_URI = "/api/v1/leave"`

---

## 데이터베이스 테이블/컬럼 명명 규칙

### 1. 테이블명
- **규칙**: UPPER_SNAKE_CASE (모두 대문자, 언더스코어로 구분하지만 구현체는 소문자)
- **예시**:
  - `USERS` - 사용자
  - `USER_ATTENDANCE` - 사용자 근태 기록
  - `USER_ATTENDANCE_CHANGE_REQUEST` - 근태 수정 요청
  - `USER_LEAVE_REQUEST` - 휴가/외출/출장 요청
  - `USER_LOGIN_HISTORY` - 사용자 로그인 이력

### 2. 컬럼명
- **규칙**: lower_snake_case
- **예시**:
  - `id` - 기본키
  - `login_id` - 로그인 ID
  - `user_name` - 사용자 이름
  - `leave_type` - 휴가 타입
  - `request_date` - 요청 날짜
  - `period_type` - 기간 타입
  - `created_at` - 생성일시
  - `updated_at` - 수정일시

### 3. Enum 컬럼
- **규칙**: UPPER_SNAKE_CASE 값 사용
- **예시**:
  - `leave_type`: `ANNUAL_LEAVE`, `BUSINESS_TRIP`, `FIELD_WORK`
  - `period_type`: `ALL_DAY`, `MORNING`, `AFTERNOON`
  - `status`: `REQUEST`, `APPROVE`, `REJECT`

---

## 공통 약어 정리

### 1. 도메인 약어
| 약어 | 전체명 | 설명 |
|------|------|------|
| att | Attendance | 근태, 출퇴근 관련 |
| lv | Leave | 휴가, 외출 관련 |
| usr | User | 사용자 관련 |
| hist | History | 이력 관련 |

### 2. 페이지 번호 규칙
| 번호 | 의미 | 예시 |
|------|------|------|
| 01 | 조회/수정 페이지 | att01 (근태 수정 요청) |
| 02 | 등록 페이지 | att02 (휴가/외출 신청) |
| 03 | 목록 조회 페이지 | att03 (휴가/외출 신청 조회) |
| 04 | 통계 페이지 | att04 (근무 일수 통계) |

### 3. 도메인 용어
| 영어 | 한글 |
|------|------|
| Attendance | 근태, 출퇴근 |
| Leave | 휴가, 외출 |
| Request | 요청 |
| Approve | 승인 |
| Reject | 반려 |
| CheckIn | 출근 |
| CheckOut | 퇴근 |
| AnnualLeave | 휴가 |
| FieldWork | 외근 |
| BusinessTrip | 출장 |
| AllDay | 종일 |
| Morning | 오전 |
| Afternoon | 오후 |

### 4. 기술 스택 약어
| 약어 | 전체명 |
|------|------|
| DTO | Data Transfer Object |
| SP | Search Parameters |
| API | Application Programming Interface |
| URI | Uniform Resource Identifier |
| URL | Uniform Resource Locator |
| CRUD | Create, Read, Update, Delete |

---

## 명명 규칙 적용 예시

### 휴가/외출 신청 기능 구현 시 파일명

```
Backend (Java):
   Entity: UserLeaveRequest.java
   DTO: UserLeaveRequestDTO.java, UserLeaveRequestResponseDTO.java
   Repository: UserLeaveRequestRepository.java
   Service: UserLeaveRequestService.java, UserLeaveRequestServiceImpl.java
   Controller: LeaveApiController.java, LeaveViewController.java

Frontend (HTML/JS):
   View: att02.html (휴가/외출 신청)
   View: att03.html (휴가/외출 신청 조회)
   Modal: att02-m01.html (신청 모달)
   Script: att02.js (신청 메인 스크립트)
   Script: att02-m01.js (신청 모달 스크립트)
   Script: att03.js (조회 메인 스크립트)

API Endpoints:
   POST /api/v1/leave/request (휴가/외출 신청)
   GET /api/v1/leave/request/all (모든 신청 조회)

View URLs:
   GET /view/attendance02 (휴가/외출 신청 페이지)
   GET /view/attendance03 (휴가/외출 신청 조회 페이지)

Database:
   Table: USER_LEAVE_REQUEST
       id (기본키)
       user_id (사용자 ID)
       leave_type (휴가 타입)
       request_date (신청 날짜)
       period_type (기간 타입)
       location (장소)
       reason (사유)
       status (상태)
       approver_id (승인자 ID)
       approved_at (승인일시)
       created_at (생성일시)
       updated_at (수정일시)
```

---

## 명명 규칙 체크리스트

코드를 작성한 후 다음 사항을 확인하세요:

- [ ] Entity 클래스명이 도메인명을 명확하게 표현하는가?
- [ ] DTO 클래스명에 DTO 접미사가 올바르게 붙었는가?
- [ ] Repository 인터페이스명에 Repository 접미사가 붙었는가?
- [ ] Service 인터페이스와 구현체 명명이 규칙을 따르는가?
- [ ] Controller 클래스명이 역할에 따라 접미사가 붙었는가?
- [ ] HTML 파일명이 약어+번호 형식인가?
- [ ] JavaScript 파일명이 HTML과 매칭되는 명명 규칙을 따르는가?
- [ ] API URI가 /api/v1/{도메인}/{동작} 형식인가?
- [ ] View URL이 /view/{도메인}{번호} 형식인가?
- [ ] 메서드명이 camelCase이며 적절한 접두어가 붙었는가?
- [ ] 변수명이 camelCase인가?
- [ ] 상수명이 UPPER_SNAKE_CASE인가?
- [ ] 테이블명이 UPPER_SNAKE_CASE인가?
- [ ] 컬럼명이 lower_snake_case인가?

---

## 참고사항

이 명명 규칙은 코드의 가독성과 일관성을 위한 가이드라인입니다.
팀의 합의 하에 예외 사항이 있을 수 있으며 변경될 수 있습니다.
