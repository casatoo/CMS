# 프로젝트 구조/프로세스 개선 리뷰

작성일: 2026-02-19

## 1) 현재 상태 요약

- 백엔드는 Spring Boot 3.2.5 + Java 17 기반이며, JPA/MyBatis, Security(JWT), Caffeine Cache, Resilience4j 등을 함께 사용 중
- 기능 범위(근태/휴가/사용자/대시보드) 대비 문서가 잘 정리되어 있고(README, docs), 패키지도 역할별로 구분되어 있음
- 다만, 운영 안정성/유지보수성 관점에서 다음 항목은 우선 개선이 필요함

## 2) 우선 개선 포인트 (High Impact)

### A. 테스트 전략 보강 (최우선)

**관찰**
- 테스트 코드가 `contextLoads` 1건만 존재

**리스크**
- 인증/권한, 근태 비즈니스 규칙, 승인 워크플로우 회귀(regression) 검출이 어려움

**개선 제안**
- 테스트 피라미드 도입
  - 단위 테스트: `serviceImpl`의 규칙(예: 근무시간 계산, 승인 상태 변경)
  - 슬라이스 테스트: `@WebMvcTest`로 API 권한/응답 스키마 검증
  - 통합 테스트: `@SpringBootTest` + Testcontainers(MySQL)로 JPA 쿼리/트랜잭션 검증
- 품질 게이트
  - PR 기준: 핵심 서비스/컨트롤러 최소 커버리지 기준 설정(예: line 60%, changed files 80%)

### B. 보안/운영 설정 분리 강화

**관찰**
- `application.yml`에 `management.endpoints.web.exposure.include: '*'` 설정
- JWT secret에 기본값(`secretKey`)이 존재
- 로깅 레벨에 SQL/바인딩 TRACE가 포함되어 있어 운영 반영 시 과도 로그 가능

**리스크**
- 액추에이터 노출 확대, 기본 시크릿 오용, 민감 로그 노출

**개선 제안**
- `application-dev.yml`, `application-prod.yml` 분리
- 프로덕션에서 actuator 최소 노출(health, info 등)
- JWT secret 기본값 제거 및 미설정 시 기동 실패 처리
- SQL TRACE 로그는 개발 프로필 한정

### C. 빌드/의존성 정리

**관찰**
- `spring-boot-starter-web`와 `spring-boot-starter-webflux` 동시 사용
- Resilience4j starter가 reactor/non-reactor 2종 동시 사용

**리스크**
- 불필요한 의존성 증가, 오토컨피그 충돌 가능성, 부팅/메모리 오버헤드

**개선 제안**
- MVC 중심이면 WebFlux 의존성 제거
- Resilience4j starter는 실제 사용 스택 1종으로 통일
- 분기별 의존성 점검(중복/미사용 제거)

## 3) 구조적 개선 포인트 (중기)

### D. 패키지 구조 개선 (기능 중심으로 전환)

**관찰**
- 현재는 `controller/service/serviceImpl/repository/dto`의 계층 중심 구조

**개선 제안**
- 점진적으로 도메인(기능) 중심 패키징으로 전환
  - 예: `attendance`, `leave`, `user` 하위에 controller/service/repository를 묶음
- 장점: 기능 단위 응집도 향상, 변경 영향도 파악 용이

### E. 서비스 구현 네이밍 개선

**관찰**
- `serviceImpl` 패키지 + `*ServiceImpl` 네이밍 사용

**개선 제안**
- 단일 구현체 중심이면 인터페이스/구현체 분리의 실익을 재점검
- 필요 시 `service` 패키지 내부로 구현 이동 또는 포트/어댑터 아키텍처 명확화

### F. API 스펙/계약 관리

**관찰**
- API 경로 상수화는 되어 있으나 계약 문서 자동화 흔적이 적음

**개선 제안**
- springdoc-openapi 도입해 Swagger UI/스키마 자동화
- 주요 API의 request/response 예시와 에러 코드 표준화

## 4) 개발 프로세스 개선 포인트

### G. CI 파이프라인 도입

**관찰**
- 저장소 기준 CI 설정 파일(.github/workflows) 부재

**개선 제안**
- 최소 파이프라인
  1. `./gradlew clean test`
  2. 정적 분석(Checkstyle/SpotBugs/PMD 중 택1)
  3. 의존성 취약점 스캔(OWASP Dependency Check 또는 Snyk/GitHub Dependabot)

### H. 코드 규칙 자동화

**관찰**
- 명명 규칙 문서는 잘 되어 있으나 자동 검증 체계는 약함

**개선 제안**
- Spotless + EditorConfig + Checkstyle로 PR 단계 자동 검증
- pre-commit hook(선택)으로 포맷/간단 검증 선반영

### I. 저장소 위생 개선

**관찰**
- `.DS_Store` 파일이 추적 대상에 존재

**개선 제안**
- `.gitignore`에 `.DS_Store` 추가
- 이미 추적된 파일은 인덱스에서 제거 후 커밋

## 5) 적용하면 좋은 기술 요소

- **Testcontainers (MySQL)**: DB 의존 통합 테스트 표준화
- **Flyway 또는 Liquibase**: 스키마 변경 이력/재현성 확보
- **OpenAPI(springdoc)**: 프론트/백엔드 계약 정합성 강화
- **Micrometer + Prometheus + Grafana**: 성능/장애 관측성 향상
- **ArchUnit**: 아키텍처 규칙(패키지 의존 방향) 자동 검증

## 6) 30일 실행 로드맵 (권장)

1주차
- 프로필 분리(dev/prod), 민감 설정 안전화(JWT/actuator/log)
- CI 기본 파이프라인 구축

2주차
- 핵심 서비스 2~3개 단위 테스트 및 WebMvcTest 추가
- 중복/미사용 의존성 정리

3주차
- Testcontainers 통합 테스트 3~5개 구축
- OpenAPI 문서 자동 생성 적용

4주차
- 기능 중심 패키지 전환 파일럿(attendance 도메인부터)
- 코드 규칙 자동화(Spotless/Checkstyle) 적용

---

## 결론

현재 프로젝트는 기능/문서 기반은 양호하지만, **테스트·보안 프로필 분리·CI 자동화**가 우선 보완되어야 장기 유지보수 비용을 크게 줄일 수 있습니다. 위 항목을 순차적으로 반영하면 안정성과 개발 속도를 동시에 높일 수 있습니다.
