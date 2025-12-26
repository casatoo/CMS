package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.UserLoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 사용자 로그인 이력 Repository
 *
 * <p>MyBatis에서 JPA로 마이그레이션 완료</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
public interface UserLoginHistoryRepository extends BaseRepository<UserLoginHistory, String> {

    /**
     * 사용자 ID로 로그인 이력 조회 (MyBatis: findByUserId)
     *
     * <p>로그인 시간 내림차순 정렬</p>
     *
     * @param userId 사용자 ID (loginId)
     * @return 로그인 이력 목록
     */
    @Query("""
        SELECT lh FROM UserLoginHistory lh
        JOIN FETCH lh.user u
        WHERE u.id = :userId
        ORDER BY lh.loginTime DESC
    """)
    List<UserLoginHistory> findByUserId(@Param("userId") String userId);
}
