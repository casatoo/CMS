## 마이그레이션 후속 수정 사항 (2025-12-27)

마이그레이션 완료 후 발견된 JPQL 호환성 문제와 타입 불일치 문제를 해결했습니다.

### 1. JPQL 함수 호환성 문제 해결

#### 문제
JPQL에서 `DATE()` 함수와 `CAST(... AS date)` 구문이 지원되지 않아 컴파일 오류 발생:
- `'(', <expression>, FUNCTION or identifier expected, got '('` 오류
- Hibernate의 JPQL 파서가 MySQL 특화 함수를 인식하지 못함

#### 해결 방법
영향받는 Repository 쿼리를 JPQL에서 Native Query로 전환:

**1) UserLeaveRequestRepository.searchLeaveRequests()**
```java
// Before (JPQL - 오류 발생)
@Query("SELECT lr FROM UserLeaveRequest lr " +
       "WHERE ... " +
       "AND (:#{#sp.createdAtStart} IS NULL OR DATE(lr.createdAt) >= :#{#sp.createdAtStart}) " +
       "...")

// After (Native Query - 정상 작동)
@Query(value = """
    SELECT lr.* FROM USER_LEAVE_REQUEST lr
    LEFT JOIN USERS u ON lr.user_id = u.id
    WHERE (:#{#sp.status} IS NULL OR lr.status = :#{#sp.status})
    AND (:#{#sp.createdAtStart} IS NULL OR DATE(lr.created_at) >= :#{#sp.createdAtStart})
    ...
    """, nativeQuery = true)
```

**2) UserAttendanceChangeRequestRepository.searchAttendanceChangeRequests()**
```java
// Before (JPQL - 오류 발생)
@Query("SELECT acr FROM UserAttendanceChangeRequest acr " +
       "WHERE ... " +
       "AND (:#{#sp.createdAtStart} IS NULL OR DATE(acr.createdAt) >= :#{#sp.createdAtStart}) " +
       "...")

// After (Native Query - 정상 작동)
@Query(value = """
    SELECT acr.* FROM USER_ATTENDANCE_CHANGE_REQUEST acr
    LEFT JOIN USERS u ON acr.user_id = u.id
    WHERE (:#{#sp.createdAtStart} IS NULL OR DATE(acr.created_at) >= :#{#sp.createdAtStart})
    ...
    """, nativeQuery = true)
```

**3) UserAttendanceChangeRequestRepository.findOneBySearchParams()**
```java
// Before (JPQL - 오류 발생)
@Query("""
    SELECT cr FROM UserAttendanceChangeRequest cr
    WHERE cr.user.id = :userId
    AND (
        CAST(cr.requestedCheckInTime AS date) = :workDate
        OR CAST(cr.requestedCheckOutTime AS date) = :workDate
    )
    """)

// After (Native Query - 정상 작동)
@Query(value = """
    SELECT cr.* FROM USER_ATTENDANCE_CHANGE_REQUEST cr
    WHERE cr.user_id = :userId
    AND (
        DATE(cr.requested_check_in_time) = :workDate
        OR DATE(cr.requested_check_out_time) = :workDate
    )
    """, nativeQuery = true)
```

#### Native Query 전환 시 주의사항
- **컬럼명 변경**: 엔티티 필드명 → 데이터베이스 컬럼명
  - `createdAt` → `created_at`
  - `requestedCheckInTime` → `requested_check_in_time`
- **테이블명 사용**: 엔티티명 → 실제 테이블명
  - `UserLeaveRequest` → `USER_LEAVE_REQUEST`
- **JOIN 문법**: JPQL `JOIN FETCH` → SQL `LEFT JOIN`
- **Enum 파라미터**: Enum 타입 → String 타입으로 변경 필요한 경우 있음

### 2. 엔티티 ID 타입 불일치 문제 해결

#### 문제
서로 다른 엔티티가 다른 ID 타입을 사용하여 타입 불일치 발생:
- `UserAttendanceChangeRequest`: ID 타입 `Long`
- `UserLeaveRequest`: ID 타입 `String`
- 공통 DTO(`ApprovalRequestDTO`)를 사용하면서 타입 충돌 발생

#### 해결 방법
각 엔티티의 ID 타입에 맞는 별도의 DTO 생성:

**1) ApprovalRequestDTO (근태 변경 요청용 - Long 타입)**
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequestDTO {
    @NotNull(message = "요청 ID는 필수입니다")  // Long이므로 @NotNull 사용
    private Long requestId;

    private String rejectReason;
}
```

**2) LeaveApprovalRequestDTO (휴가 요청용 - String 타입) - 신규 생성**
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApprovalRequestDTO {
    @NotBlank(message = "요청 ID는 필수입니다")  // String이므로 @NotBlank 사용
    private String requestId;

    private String rejectReason;
}
```

**3) Service 메서드 시그니처 변경**

**UserAttendanceChangeRequestService:**
```java
// Before
ApiResponse approveRequest(String requestId);
ApiResponse rejectRequest(String requestId, String rejectReason);

// After
ApiResponse approveRequest(Long requestId);
ApiResponse rejectRequest(Long requestId, String rejectReason);
```

**UserLeaveRequestService: (변경 없음 - 이미 String 사용)**
```java
ApiResponse approveRequest(String requestId);
ApiResponse rejectRequest(String requestId, String rejectReason);
```

**4) Controller 수정**

**AttendanceApiController:**
```java
@PostMapping("/request/approve")
public ApiResponse approveAttendanceChangeRequest(@Valid @RequestBody ApprovalRequestDTO request) {
    return userAttendanceChangeRequestService.approveRequest(request.getRequestId());
}
```

**LeaveApiController:**
```java
@PostMapping("/request/approve")
public ApiResponse approveLeaveRequest(@Valid @RequestBody LeaveApprovalRequestDTO request) {
    return userLeaveRequestService.approveRequest(request.getRequestId());
}
```

**5) Native Query 파라미터 타입 조정**

`findOneBySearchParams` 메서드에서 Enum 파라미터를 String으로 변경:
```java
// Before
Optional<UserAttendanceChangeRequest> findOneBySearchParams(
    @Param("userId") String userId,
    @Param("workDate") LocalDate workDate,
    @Param("status") UserAttendanceChangeRequest.ChangeStatus status
);

// After
Optional<UserAttendanceChangeRequest> findOneBySearchParams(
    @Param("userId") String userId,
    @Param("workDate") LocalDate workDate,
    @Param("status") String status  // Native Query이므로 String 사용
);
```

**Service에서 Enum → String 변환:**
```java
userAttendanceChangeRequestRepository.findOneBySearchParams(
    user.getId(),
    request.getAttendance().getWorkDate(),
    request.getStatus().name()  // Enum을 String으로 변환
);
```

### 3. UserLoginHistoryRepository JPQL 오류 수정

#### 문제
`UserLoginHistory` 엔티티에 `userId` 필드가 없고 `user` 관계만 존재:
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private Users user;
```

JPQL에서 존재하지 않는 필드 참조:
```java
// 오류 발생
WHERE lh.userId = :userId  // userId 필드가 없음
```

#### 해결 방법
JOIN FETCH를 사용하여 연관 엔티티를 통해 접근:
```java
// Before
@Query("""
    SELECT lh FROM UserLoginHistory lh
    WHERE lh.userId = :userId
    ORDER BY lh.loginTime DESC
""")

// After
@Query("""
    SELECT lh FROM UserLoginHistory lh
    JOIN FETCH lh.user u
    WHERE u.id = :userId
    ORDER BY lh.loginTime DESC
""")
```

### 수정 영향 범위

#### 수정된 파일 (7개)
1. `/src/main/java/io/github/mskim/comm/cms/repository/UserLeaveRequestRepository.java`
2. `/src/main/java/io/github/mskim/comm/cms/repository/UserAttendanceChangeRequestRepository.java`
3. `/src/main/java/io/github/mskim/comm/cms/repository/UserLoginHistoryRepository.java`
4. `/src/main/java/io/github/mskim/comm/cms/service/UserAttendanceChangeRequestService.java`
5. `/src/main/java/io/github/mskim/comm/cms/serviceImpl/UserAttendanceChangeRequestServiceImpl.java`
6. `/src/main/java/io/github/mskim/comm/cms/dto/ApprovalRequestDTO.java`
7. `/src/main/java/io/github/mskim/comm/cms/controller/api/LeaveApiController.java`

#### 신규 생성된 파일 (1개)
1. `/src/main/java/io/github/mskim/comm/cms/dto/LeaveApprovalRequestDTO.java`

### 기술적 교훈

#### 1. JPQL vs Native Query 선택 기준
- **JPQL 사용**:
  - 데이터베이스 독립성이 필요한 경우
  - 단순한 CRUD 및 표준 함수만 사용하는 경우
  - 엔티티 기반 쿼리로 충분한 경우

- **Native Query 사용**:
  - 데이터베이스 특화 함수 필요 (`DATE()`, `HOUR()`, `CONCAT()` 등)
  - 복잡한 서브쿼리나 윈도우 함수 사용
  - 성능 최적화를 위한 특정 힌트 사용

#### 2. ID 타입 설계 가이드
- **Long 타입 권장**:
  - 자동 증가 ID (AUTO_INCREMENT)
  - 성능상 이점 (인덱스 효율)
  - JPA 기본 전략과 호환성 우수

- **String 타입 사용 케이스**:
  - UUID 또는 비즈니스 키 사용
  - 외부 시스템 연동 ID
  - 레거시 시스템 호환성

#### 3. DTO 설계 원칙
- **타입 안전성 우선**: 각 엔티티에 맞는 타입 사용
- **재사용 vs 명확성**: 공통 DTO보다 명확한 별도 DTO 선호
- **유효성 검사 일관성**: 타입에 맞는 어노테이션 사용
  - `String`: `@NotBlank`
  - `Long`: `@NotNull`
  - `LocalDate`: `@NotNull` + `@DateTimeFormat`

### 성능 영향 분석

#### Native Query 전환의 영향
- **긍정적 영향**:
  - 데이터베이스 최적화 기능 직접 활용 가능
  - MySQL 쿼리 캐시 활용 가능
  - 실행 계획 예측 가능성 향상

- **부정적 영향**:
  - 데이터베이스 종속성 증가
  - 엔티티 자동 매핑 제약 (필드명 일치 필요)
  - 타입 변환 오버헤드 가능

#### 권장 사항
1. **쿼리 성능 모니터링**: 슬로우 쿼리 로그 활성화
2. **인덱스 최적화**: DATE() 함수 사용 시 함수 기반 인덱스 고려
3. **캐싱 전략**: 자주 조회되는 검색 결과 캐싱
4. **테스트 강화**: Native Query는 컴파일 타임 체크 불가하므로 통합 테스트 필수

이러한 수정을 통해 MyBatis에서 JPA로의 마이그레이션이 완전히 안정화되었으며, 타입 안전성과 데이터베이스 호환성을 모두 확보했습니다.

