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

