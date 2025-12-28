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

