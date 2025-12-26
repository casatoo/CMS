package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.UserAttendanceChangeRequest;
import io.github.mskim.comm.cms.sp.UserAttendanceChangeRequestSP;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 근태 수정 요청 Repository
 *
 * <p>MyBatis에서 JPA로 마이그레이션 완료</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
public interface UserAttendanceChangeRequestRepository extends BaseRepository<UserAttendanceChangeRequest, String> {

    // ========================================
    // 기존 검색 쿼리 (유지)
    // ========================================

    @Query(value = """
           SELECT acr.* FROM USER_ATTENDANCE_CHANGE_REQUEST acr
           LEFT JOIN USERS u ON acr.user_id = u.id
           WHERE (:#{#sp.status} IS NULL OR acr.status = :#{#sp.status})
           AND (:#{#sp.userName} IS NULL OR u.name LIKE CONCAT('%', :#{#sp.userName}, '%'))
           AND (:#{#sp.workDateStart} IS NULL OR acr.work_date >= :#{#sp.workDateStart})
           AND (:#{#sp.workDateEnd} IS NULL OR acr.work_date <= :#{#sp.workDateEnd})
           AND (:#{#sp.createdAtStart} IS NULL OR DATE(acr.created_at) >= :#{#sp.createdAtStart})
           AND (:#{#sp.createdAtEnd} IS NULL OR DATE(acr.created_at) <= :#{#sp.createdAtEnd})
           ORDER BY acr.created_at DESC
           """, nativeQuery = true)
    List<UserAttendanceChangeRequest> searchAttendanceChangeRequests(@Param("sp") UserAttendanceChangeRequestSP sp);

    // ========================================
    // MyBatis 쿼리 변환 (JOIN FETCH 추가)
    // ========================================

    /**
     * 사용자별 변경 요청 전체 조회 (MyBatis: findAllByUserId)
     *
     * <p>User, Attendance, Approver 정보를 함께 Fetch Join으로 조회</p>
     *
     * @param userId 사용자 ID
     * @return 변경 요청 목록
     */
    @Query("""
        SELECT cr FROM UserAttendanceChangeRequest cr
        JOIN FETCH cr.user u
        JOIN FETCH cr.attendance a
        LEFT JOIN FETCH cr.approver ap
        WHERE cr.user.id = :userId
        ORDER BY cr.createdAt DESC
    """)
    List<UserAttendanceChangeRequest> findAllByUserId(@Param("userId") String userId);

    /**
     * 승인 대기 목록 조회 (MyBatis: findPendingRequests)
     *
     * <p>상태가 'REQUEST'인 모든 요청 조회</p>
     *
     * @return 승인 대기 중인 요청 목록
     */
    @Query("""
        SELECT cr FROM UserAttendanceChangeRequest cr
        JOIN FETCH cr.user u
        JOIN FETCH cr.attendance a
        LEFT JOIN FETCH cr.approver ap
        WHERE cr.status = 'REQUEST'
        ORDER BY cr.createdAt ASC
    """)
    List<UserAttendanceChangeRequest> findPendingRequests();

    /**
     * 단일 요청 상세 조회 (MyBatis: findById)
     *
     * <p>User, Attendance, Approver 정보를 함께 Fetch Join으로 조회</p>
     *
     * @param id 요청 ID
     * @return 변경 요청 (Optional)
     */
    @Query("""
        SELECT cr FROM UserAttendanceChangeRequest cr
        JOIN FETCH cr.user u
        JOIN FETCH cr.attendance a
        LEFT JOIN FETCH cr.approver ap
        WHERE cr.id = :id
    """)
    Optional<UserAttendanceChangeRequest> findByIdWithDetails(@Param("id") String id);

    /**
     * 검색 조건으로 단일 요청 조회 (MyBatis: findOneBySearchParams)
     *
     * <p>특정 사용자의 특정 날짜에 해당하는 특정 상태의 요청 조회</p>
     *
     * @param userId 사용자 ID
     * @param workDate 근무일
     * @param status 요청 상태
     * @return 변경 요청 (Optional)
     */
    @Query(value = """
        SELECT cr.* FROM USER_ATTENDANCE_CHANGE_REQUEST cr
        WHERE cr.user_id = :userId
        AND (
            DATE(cr.requested_check_in_time) = :workDate
            OR DATE(cr.requested_check_out_time) = :workDate
        )
        AND cr.status = :status
    """, nativeQuery = true)
    Optional<UserAttendanceChangeRequest> findOneBySearchParams(
        @Param("userId") String userId,
        @Param("workDate") LocalDate workDate,
        @Param("status") String status
    );

    // ========================================
    // 추가 유틸리티 메서드 (Query Method 방식)
    // ========================================

    /**
     * 상태별 요청 조회
     *
     * @param status 요청 상태
     * @return 해당 상태의 요청 목록
     */
    List<UserAttendanceChangeRequest> findByStatusOrderByCreatedAtDesc(
        UserAttendanceChangeRequest.ChangeStatus status
    );

    /**
     * 상태별 요청 개수 조회
     *
     * @param status 요청 상태
     * @return 요청 개수
     */
    Long countByStatus(UserAttendanceChangeRequest.ChangeStatus status);
}
