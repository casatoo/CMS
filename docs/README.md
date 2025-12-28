# CMS 프로젝트 문서

이 폴더에는 CMS 프로젝트의 상세 기술 문서가 포함되어 있습니다.

## 문서 구조

### 📁 루트 문서
- **[MIGRATION_HISTORY.md](./MIGRATION_HISTORY.md)** - MyBatis에서 JPA로의 완전한 마이그레이션 히스토리
- **[MIGRATION_FIXES.md](./MIGRATION_FIXES.md)** - 마이그레이션 후속 수정 사항 (JPQL 호환성, 타입 불일치 해결)
- **[ARCHITECTURE_REFACTORING.md](./ARCHITECTURE_REFACTORING.md)** - 아키텍처 리팩토링 상세 (캐싱, N+1 해결, 성능 최적화)
- **[NAMING_CONVENTIONS_FROM_CLAUDE.md](./NAMING_CONVENTIONS_FROM_CLAUDE.md)** - CLAUDE.md에서 추출한 명명 규칙

### 📁 features/ - 기능 문서
- **[SEARCH.md](./features/SEARCH.md)** - 검색 기능 구현 상세
  - SpEL 기반 동적 쿼리
  - 프론트엔드 공통 함수 라이브러리
  - JavaScript 리팩토링

- **[PAGINATION.md](./features/PAGINATION.md)** - 서버사이드 페이지네이션
  - Spring Data JPA Specification
  - PageResponse DTO
  - AG-Grid 연동 계획

### 📁 security/ - 보안 문서
- **[SECURITY_IMPROVEMENTS.md](./security/SECURITY_IMPROVEMENTS.md)** - 보안 개선 사항 (2025-10-03)
  - JWT 토큰 만료 버그 수정
  - 환경 변수 외부화
  - 입력 유효성 검사 강화
  - 보안 모범 사례

### 📁 performance/ - 성능 문서 (생성 예정)
- **QUERY_OPTIMIZATION.md** - 쿼리 최적화 가이드
  - 슬로우 쿼리 분석
  - 실행 계획 분석
  - 인덱스 전략

- **INDEX_STRATEGY.md** - 데이터베이스 인덱스 전략
  - 테이블별 인덱스 설계
  - 성능 측정 결과

## 문서 읽기 순서 (신규 개발자용)

1. **[../CLAUDE.md](../CLAUDE.md)** - 프로젝트 개요 및 기술 스택 (먼저 읽기)
2. **[../NAMING_CONVENTIONS.md](../NAMING_CONVENTIONS.md)** - 명명 규칙 (코딩 시작 전)
3. **[ARCHITECTURE_REFACTORING.md](./ARCHITECTURE_REFACTORING.md)** - 현재 아키텍처 이해
4. **[features/](./features/)** - 주요 기능 구현 상세
5. **[security/SECURITY_IMPROVEMENTS.md](./security/SECURITY_IMPROVEMENTS.md)** - 보안 가이드

## 문서 최적화 결과

### Before (단일 CLAUDE.md)
- **라인 수**: 2,742줄
- **파일 크기**: ~105KB
- **토큰 수**: ~39,000 토큰

### After (분리된 구조)
- **CLAUDE.md**: 203줄 (7.2KB) - **92.6% 감소**
- **상세 문서**: 7개 파일 (84KB)
- **총 크기**: ~91KB (구조화 및 가독성 향상)

## 문서 업데이트 가이드

새로운 주요 기능이나 변경 사항이 있을 때:

1. **해당 카테고리 폴더에 문서 생성**
   - 기능: `features/`
   - 보안: `security/`
   - 성능: `performance/`

2. **CLAUDE.md에 요약 추가**
   - "최근 주요 변경 사항" 섹션에 날짜와 요약 추가
   - 상세 문서 링크 제공

3. **docs/README.md 업데이트**
   - 새 문서를 적절한 섹션에 추가

---

**Last Updated**: 2025-12-28
