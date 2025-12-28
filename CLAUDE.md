# CMS - 콘텐츠 관리 시스템

## 개요
직원 근태 관리 및 사용자 관리에 중점을 둔 Spring Boot 기반의 콘텐츠 관리 시스템(CMS)입니다. 이 시스템은 근태 추적, 휴가 신청 관리 및 사용자 인증을 위한 웹 기반 인터페이스를 제공합니다.

## 기술 스택

### 백엔드 프레임워크
- **Spring Boot 3.2.5** - 메인 애플리케이션 프레임워크
- **Java 17** - 프로그래밍 언어 (Amazon Corretto 17.0.10)
- **Gradle 8.7** - 빌드 도구 및 의존성 관리

### 핵심 기술
- **Spring Data JPA** - 데이터베이스 추상화 계층 (단일 ORM 패턴)
- **Spring Security 6** - JWT 기반 인증 및 권한 부여
- **MySQL/MariaDB** - 주 데이터베이스
- **Thymeleaf** - 서버 사이드 템플릿 엔진
- **Spring Cache (Caffeine)** - 인메모리 캐싱
- **Resilience4j** - 서킷 브레이커 및 복원력 패턴

### 프론트엔드
- **Bootstrap 5.3.3** - 반응형 UI 프레임워크
- **AG-Grid Community** - 데이터 그리드 컴포넌트
- **jQuery 3.6.0** - DOM 조작 및 AJAX
- **FullCalendar** - 캘린더 컴포넌트
- **Flatpickr** - 날짜 선택기 (한국어 지원)
- **SweetAlert2** - 팝업 알림
- **ApexCharts** - 인터랙티브 차트 라이브러리

## 프로젝트 구조

```
src/
├── main/
│   ├── java/io/github/mskim/comm/cms/
│   │   ├── CmsApplication.java           # 메인 Spring Boot 애플리케이션
│   │   ├── config/                       # 구성 클래스
│   │   ├── controller/                   # 웹 컨트롤러
│   │   │   ├── api/                      # REST API 컨트롤러
│   │   │   └── view/                     # 뷰 컨트롤러 (Thymeleaf)
│   │   ├── dto/                          # 데이터 전송 객체
│   │   ├── entity/                       # JPA 엔티티
│   │   ├── repository/                   # JPA 리포지토리
│   │   ├── service/                      # 서비스 인터페이스
│   │   ├── serviceImpl/                  # 서비스 구현
│   │   ├── util/                         # 유틸리티 클래스
│   │   └── exception/                    # 예외 처리
│   └── resources/
│       ├── application.yml               # 메인 구성
│       ├── static/                       # 정적 웹 리소스
│       └── templates/                    # Thymeleaf 템플릿
└── test/                                 # 테스트 소스
```

## 핵심 엔티티

### 사용자 관리
- **Users** - 사용자 정보 및 인증
- **UserProfile** - 사용자 프로필 및 프로필 사진
- **UserLoginHistory** - 로그인 이력

### 근태 관리
- **UserAttendance** - 일일 근태 기록 (출근/퇴근)
- **UserAttendanceChangeRequest** - 근태 수정 요청
- **UserLeaveRequest** - 휴가/연차/외근/출장 요청

## 주요 기능

### 인증 및 보안
- JWT 기반 상태 비저장 인증
- 역할 기반 접근 제어 (ROLE_USER, ROLE_ADMIN)
- 입력 유효성 검사 및 보안 강화
- 환경 변수를 통한 민감 정보 관리

### 근태 시스템
- 출퇴근 시간 추적 및 기록
- 월별 근무일 수 집계
- 근태 수정 요청 및 승인 워크플로우
- 휴가/외근/출장 신청 및 관리
- 다중 조건 검색 및 필터링
- 서버사이드 페이지네이션

### 관리자 대시보드
- 실시간 출근 현황 모니터링 (출근/미출근/출근율)
- 승인 대기 현황 (근태 수정 요청, 휴가 신청)
- ApexCharts 기반 시각화 (도넛 차트, 막대 차트)
- 자동 갱신 (5분 간격)
- 관리자 전용 접근 (ROLE_ADMIN)

### 성능 최적화
- Caffeine 기반 인메모리 캐싱
  - 사용자별 캐시 분리 (loginId, userId 기반 키)
  - 로그아웃 시 전체 캐시 자동 클리어
  - 브라우저 캐시 방지 헤더 설정
- JOIN FETCH를 통한 N+1 쿼리 해결
- 데이터베이스 인덱스 최적화
- AOP 기반 쿼리 성능 모니터링

## 개발 환경 설정

### 사전 요구 사항
- JDK 17 (Amazon Corretto 권장)
- MySQL/MariaDB 서버
- Gradle 8.7 (또는 래퍼 사용)

### 환경 변수 설정
```bash
# 데이터베이스
export DB_USERNAME=your_database_username
export DB_PASSWORD=your_secure_database_password

# JWT
export JWT_SECRET=your_strong_jwt_secret_key

# JPA DDL
export DDL_AUTO=update  # 프로덕션: validate
```

### 실행
```bash
# 개발 서버 시작
./gradlew bootRun

# 프로덕션 빌드
./gradlew build

# 테스트 실행
./gradlew test
```

## 아키텍처 패턴

### 계층화된 아키텍처
- **Controller Layer**: HTTP 요청/응답 처리
- **Service Layer**: 비즈니스 로직 및 트랜잭션 관리
- **Repository Layer**: 데이터 액세스 (JPA)
- **Entity Layer**: 도메인 모델

### 주요 디자인 패턴
- **Repository Pattern**: Spring Data JPA
- **DTO Pattern**: 계층 간 데이터 전송
- **Specification Pattern**: 동적 쿼리 생성
- **Builder Pattern**: 객체 생성 (Lombok)

## 명명 규칙

이 프로젝트는 일관된 명명 규칙을 따릅니다.

### 주요 규칙
- **Entity**: `{도메인명}` (예: `Users`, `UserAttendance`)
- **DTO**: `{도메인명}DTO` 또는 `{도메인명}{용도}DTO`
- **Repository**: `{도메인명}Repository`
- **Service**: `{도메인명}Service` / `{도메인명}ServiceImpl`
- **Controller**:
  - API: `{도메인명}ApiController`
  - View: `{도메인명}ViewController`

**상세 내용**: [NAMING_CONVENTIONS.md](./NAMING_CONVENTIONS.md)

## 최근 주요 변경 사항

### 2025-12-28: 관리자 대시보드 및 캐시 개선
- **관리자 대시보드 구현**:
  - 실시간 출근 현황 및 승인 대기 통계
  - ApexCharts 기반 시각화 (도넛 차트, 막대 차트)
  - 자동 갱신 기능 (5분 간격)
  - ROLE_ADMIN 권한 기반 접근 제어

- **캐시 전략 개선**:
  - 로그아웃 시 모든 캐시 자동 클리어
  - 브라우저 캐시 방지 헤더 추가
  - 사용자 전환 시 이전 사용자 데이터 표시 방지
  - 대시보드 통계 캐싱 (30분 TTL)

- **보안 강화**:
  - 캐시 키에 위치 인자 참조 방식 적용 (#p0)
  - SecurityConfig에 CacheManager 주입
  - 관리자 전용 API 엔드포인트 보호

### 2025-12-27: 아키텍처 대규모 리팩토링
- **MyBatis → JPA 완전 마이그레이션**: 단일 ORM 패턴 확립
  - [상세 내용: MIGRATION_HISTORY.md](./docs/MIGRATION_HISTORY.md)
  - [마이그레이션 수정 사항: MIGRATION_FIXES.md](./docs/MIGRATION_FIXES.md)

- **성능 최적화**:
  - N+1 쿼리 해결 (JOIN FETCH)
  - Caffeine 캐싱 도입
  - 데이터베이스 인덱스 최적화
  - [상세 내용: ARCHITECTURE_REFACTORING.md](./docs/ARCHITECTURE_REFACTORING.md)

- **서버사이드 페이지네이션**: Specification API 활용
  - [상세 내용: features/PAGINATION.md](./docs/features/PAGINATION.md)

### 2025-10-08: 검색 기능 개선
- SpEL 기반 동적 검색 쿼리
- 프론트엔드 공통 함수 라이브러리 (`apps.js`)
- [상세 내용: features/SEARCH.md](./docs/features/SEARCH.md)

### 2025-10-03: 보안 강화
- JWT 토큰 만료 버그 수정
- 환경 변수 외부화
- 입력 유효성 검사 강화
- [상세 내용: security/SECURITY_IMPROVEMENTS.md](./docs/security/SECURITY_IMPROVEMENTS.md)

## 코드 스타일 및 관례

- Lombok을 사용한 보일러플레이트 코드 최소화
- 생성자 주입 선호 (`@RequiredArgsConstructor`)
- 서비스 계층은 인터페이스 + 구현 패턴
- JOIN FETCH를 통한 N+1 문제 해결
- `@Transactional` 명시적 트랜잭션 경계
- 체계적인 예외 처리 (`ErrorCode`, `BusinessException`)

## 추가 문서

### 아키텍처 및 개발
- [아키텍처 리팩토링 상세](./docs/ARCHITECTURE_REFACTORING.md)
- [마이그레이션 히스토리](./docs/MIGRATION_HISTORY.md)
- [명명 규칙](./NAMING_CONVENTIONS.md)

### 기능 문서
- [검색 기능](./docs/features/SEARCH.md)
- [페이지네이션](./docs/features/PAGINATION.md)
- [관리자 대시보드](./docs/features/ADMIN_DASHBOARD.md) (생성 예정)

### 성능 및 보안
- [쿼리 최적화](./docs/performance/QUERY_OPTIMIZATION.md) (생성 예정)
- [인덱스 전략](./docs/performance/INDEX_STRATEGY.md) (생성 예정)
- [보안 개선 사항](./docs/security/SECURITY_IMPROVEMENTS.md)

---

**참고**: 이 프로젝트는 Spring Boot 모범 사례를 따르며, 단일 ORM 패턴(JPA), 캐싱, 보안, 그리고 성능 최적화에 중점을 둔 엔터프라이즈급 애플리케이션입니다.
