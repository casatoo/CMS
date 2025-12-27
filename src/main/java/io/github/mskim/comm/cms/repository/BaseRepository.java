package io.github.mskim.comm.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * 공통 Repository 인터페이스
 *
 * <p>모든 Repository의 기본 인터페이스로, 공통 메서드를 정의합니다.</p>
 * <p>향후 소프트 삭제, 배치 작업 등 공통 기능을 추가할 수 있습니다.</p>
 * <p>JpaSpecificationExecutor를 상속하여 동적 쿼리 및 페이징 기능을 제공합니다.</p>
 *
 * @param <T> 엔티티 타입
 * @param <ID> ID 타입 (String, Long 등)
 * @author CMS Team
 * @since 1.0.0
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * 향후 공통 메서드 추가 예정:
     *
     * - 소프트 삭제 (Soft Delete)
     * - 삭제되지 않은 엔티티만 조회
     * - 배치 작업 (Batch Operations)
     * - 감사 정보 자동 설정
     */
}
