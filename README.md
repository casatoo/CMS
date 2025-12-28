# CMS - 콘텐츠 관리 시스템

직원 근태 관리 및 사용자 관리에 중점을 둔 Spring Boot 기반의 콘텐츠 관리 시스템(CMS)입니다.

## 📋 목차

- [기술 스택](#-기술-스택)
- [주요 기능](#-주요-기능)
- [개발 환경 설정](#-개발-환경-설정)
- [프로젝트 구조](#-프로젝트-구조)
- [시작하기](#-시작하기)
- [명명 규칙](#-명명-규칙)
- [문서](#-문서)

## 🛠 기술 스택

### Backend
- **Spring Boot 3.2.5** - 메인 애플리케이션 프레임워크
- **Java 17** - Amazon Corretto 17.0.10
- **Spring Security 6** - JWT 기반 인증/권한 부여
- **Spring Data JPA** - 데이터 액세스 계층
- **MyBatis 3.0.3** - SQL 매핑 프레임워크
- **Gradle 8.7** - 빌드 도구

### Database
- **MySQL/MariaDB** - MariaDB 10.4.25

### Frontend
- **Thymeleaf** - 서버 사이드 템플릿 엔진
- **Bootstrap 5.3.3** - CSS 프레임워크
- **AG-Grid Community** - 데이터 그리드
- **FullCalendar** - 캘린더 컴포넌트
- **Flatpickr** - 날짜 선택기
- **jQuery 3.6.0** - JavaScript 라이브러리

### Security & Resilience
- **JWT (JJWT 0.12.3)** - 토큰 기반 인증
- **BCrypt** - 비밀번호 암호화
- **Resilience4j** - 서킷 브레이커, 재시도 패턴

## ✨ 주요 기능

### 인증 및 보안
- JWT 기반 상태 비저장 인증
- 쿠키 기반 토큰 저장 (HttpOnly, Secure)
- 역할 기반 접근 제어
- 포괄적인 입력 유효성 검사

### 근태 관리 시스템
- 일일 출퇴근 추적
- 8시간 근무일 계산
- 월별 근무일 수 집계
- 근태 수정 요청 및 승인 워크플로우
- 휴가/외근/출장 신청 관리
- 고급 검색 및 필터링 기능
- AG-Grid 기반 목록 뷰

## 🔧 개발 환경 설정

### 사전 요구 사항

#### 필수 소프트웨어
- **JDK**: Amazon Corretto 17.0.10 이상
- **IDE**: IntelliJ IDEA 2021.2.1 이상 (권장)
- **빌드 도구**: Gradle 8.7 (또는 래퍼 사용)
- **데이터베이스**: MySQL 8.0 또는 MariaDB 10.4 이상

#### 환경 변수 설정

프로젝트 루트에 `.env` 파일을 생성하거나 시스템 환경 변수를 설정하세요:

```bash
# 데이터베이스 구성
export DB_USERNAME=your_database_username
export DB_PASSWORD=your_secure_database_password

# JWT 구성
export JWT_SECRET=your_strong_jwt_secret_key

# 애플리케이션 구성
export DDL_AUTO=update  # 개발: update, 프로덕션: validate
```

`.env.example` 파일을 참고하여 설정할 수 있습니다.

### IntelliJ IDEA 설정

#### 1. Configurations 설정

**Run/Debug Configurations:**
- `Enable Launch Optimization`: **OFF**
  - 경로: Run → Edit Configurations → [Application] → Modify options → Enable Launch Optimization
  - 이유: 디버깅 시 안정성 확보 및 예기치 않은 동작 방지

#### 2. IntelliJ IDEA 일반 설정

**Async Stack Traces 비활성화:**
- 경로: File → Settings (Preferences on macOS) → Build, Execution, Deployment → Debugger → Async Stack Traces
- 설정: **OFF**
- 이유: 성능 향상 및 단순한 스택 트레이스

**파일 인코딩 설정:**
- 경로: File → Settings → Editor → File Encodings
- 설정:
  - Global Encoding: **UTF-8**
  - Project Encoding: **UTF-8**
  - Default encoding for properties files: **UTF-8**

**빌드 도구 설정:**
- 경로: File → Settings → Build, Execution, Deployment → Build Tools → Gradle
- 설정:
  - Build and run using: **IntelliJ IDEA**
  - Run tests using: **IntelliJ IDEA**
- 이유: Gradle Daemon보다 빠른 빌드 속도

#### 3. 추가 권장 설정

**Lombok 플러그인:**
- 경로: File → Settings → Plugins
- 설치: Lombok 플러그인 설치
- 경로: File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors
- 설정: Enable annotation processing 체크

**코드 스타일:**
- 경로: File → Settings → Editor → Code Style → Java
- Scheme: Project (프로젝트 일관성 유지)

### 데이터베이스 설정

#### MySQL/MariaDB 설치 및 설정

```sql
-- 데이터베이스 생성
CREATE DATABASE cms CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 사용자 생성 및 권한 부여 (선택사항)
CREATE USER 'cms_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON cms.* TO 'cms_user'@'localhost';
FLUSH PRIVILEGES;
```

#### application.yml 확인

`src/main/resources/application.yml` 파일에서 데이터베이스 연결 정보를 확인하세요:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cms?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Seoul
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

## 📁 프로젝트 구조

```
src/
├── main/
│   ├── java/io/github/mskim/comm/cms/
│   │   ├── CmsApplication.java           # 메인 Spring Boot 애플리케이션
│   │   ├── config/                       # 구성 클래스
│   │   ├── controller/
│   │   │   ├── api/                      # REST API 컨트롤러
│   │   │   └── view/                     # 뷰 컨트롤러 (Thymeleaf)
│   │   ├── dto/                          # 데이터 전송 객체
│   │   ├── entity/                       # JPA 엔티티
│   │   ├── jwt/                          # JWT 유틸리티 및 필터
│   │   ├── repository/                   # JPA 리포지토리
│   │   ├── service/                      # 서비스 인터페이스
│   │   ├── serviceImpl/                  # 서비스 구현
│   │   └── sp/                           # 검색 매개변수
│   └── resources/
│       ├── application.yml               # 메인 구성
│       ├── static/                       # 정적 리소스
│       │   ├── css/
│       │   ├── js/
│       │   └── img/
│       └── templates/                    # Thymeleaf 템플릿
└── test/                                 # 테스트 소스
```

## 🚀 시작하기

### 1. 프로젝트 클론

```bash
git clone <repository-url>
cd CMS
```

### 2. 환경 변수 설정

`.env.example` 파일을 복사하여 `.env` 파일을 생성하고 값을 설정하세요:

```bash
cp .env
# .env 파일을 편집하여 실제 값 입력
```

### 3. 데이터베이스 설정

MySQL/MariaDB에서 `cms` 데이터베이스를 생성하세요.

### 4. 애플리케이션 실행

#### Gradle 사용

```bash
# 개발 서버 시작
./gradlew bootRun

# 빌드
./gradlew build

# 테스트 실행
./gradlew test
```

#### IntelliJ IDEA 사용

1. `CmsApplication.java` 파일을 엽니다
2. 메인 메서드 왼쪽의 실행 버튼을 클릭합니다
3. 브라우저에서 `http://localhost:8080` 접속

### 5. 접속 확인

- **애플리케이션**: http://localhost:8080
- **기본 포트**: 8080

## 📝 명명 규칙

이 프로젝트는 일관된 코드베이스 유지를 위해 엄격한 명명 규칙을 따릅니다.

### 주요 규칙

- **Entity**: `{도메인명}` (예: `Users`, `UserAttendance`)
- **DTO**: `{도메인명}DTO` (예: `UserLeaveRequestResponseDTO`)
- **Repository**: `{도메인명}Repository`
- **Service**: `{도메인명}Service` / `{도메인명}ServiceImpl`
- **Controller**: `{도메인명}ApiController` / `{도메인명}ViewController`
- **HTML/JS**: `{약어}{번호}.html` / `{약어}{번호}.js` (예: `att01.html`, `att01.js`)
- **API URI**: `/api/v1/{도메인}/{동작}`
- **View URL**: `/view/{도메인}{번호}`

자세한 내용은 [NAMING_CONVENTIONS.md](NAMING_CONVENTIONS.md)를 참조하세요.

## 📚 문서

프로젝트에 대한 자세한 문서는 다음 파일을 참조하세요:

- **[CLAUDE.md](CLAUDE.md)** - 전체 프로젝트 문서 (기술 스택, 아키텍처, 보안, 기능 상세)
- **[NAMING_CONVENTIONS.md](NAMING_CONVENTIONS.md)** - 코드 명명 규칙 가이드

### 주요 문서 섹션

#### CLAUDE.md 주요 내용
- 기술 스택 및 오픈소스 라이브러리
- 프로젝트 구조 및 아키텍처 패턴
- 보안 개선 사항 (JWT, 입력 검증, 환경 변수)
- 검색 기능 구현 상세
- 명명 규칙 요약

#### NAMING_CONVENTIONS.md 주요 내용
- Java 클래스 명명 규칙
- HTML/JavaScript 파일 명명 규칙
- URL/URI 규칙
- 데이터베이스 테이블/컬럼 명명 규칙
- 명명 규칙 체크리스트

## 🔐 보안

### 프로덕션 배포 시 주의사항

- 환경 변수를 통한 민감 정보 관리
- `DDL_AUTO=validate` 사용 (자동 스키마 변경 방지)
- HTTPS 사용 필수 (secure cookie 작동)
- 강력한 JWT Secret 키 사용
- 정기적인 의존성 업데이트 및 보안 취약점 모니터링

### 보안 기능

- JWT 토큰 기반 인증
- BCrypt 비밀번호 암호화
- 입력 유효성 검사
- CORS 설정
- 서킷 브레이커 패턴