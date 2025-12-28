package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.UserLeaveRequest;
import io.github.mskim.comm.cms.sp.UserLeaveRequestSP;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 휴가 요청 Repository
 *
 * @author CMS Team
 * @since 1.0.0
 */
public interface UserLeaveRequestRepository extends BaseRepository<UserLeaveRequest, String> {

    /**
     * ID로 휴가 요청 조회 (User와 Approver 함께 조회, N+1 방지)
     *
     * @param id 휴가 요청 ID
     * @return 휴가 요청
     */
    @Query("""
        SELECT lr FROM UserLeaveRequest lr
        JOIN FETCH lr.user u
        LEFT JOIN FETCH lr.approver a
        WHERE lr.id = :id
    """)
    Optional<UserLeaveRequest> findByIdWithUser(@Param("id") String id);

    /**
     * 사용자별 특정 날짜의 대기 중인 휴가 요청 조회 (N+1 방지)
     *
     * @param userId 사용자 ID
     * @param requestDate 요청 날짜
     * @return 대기 중인 휴가 요청
     */
    @Query("""
        SELECT lr FROM UserLeaveRequest lr
        JOIN FETCH lr.user u
        LEFT JOIN FETCH lr.approver a
        WHERE lr.user.id = :userId
        AND lr.requestDate = :requestDate
        AND lr.status = 'REQUEST'
    """)
    Optional<UserLeaveRequest> findPendingRequestByUserIdAndDate(
        @Param("userId") String userId,
        @Param("requestDate") LocalDate requestDate
    );

    /**
     * 기간별 전체 휴가 요청 조회 (N+1 방지)
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 휴가 요청 목록
     */
    @Query("""
        SELECT lr FROM UserLeaveRequest lr
        JOIN FETCH lr.user u
        LEFT JOIN FETCH lr.approver a
        WHERE lr.requestDate BETWEEN :startDate AND :endDate
        ORDER BY lr.requestDate DESC
    """)
    List<UserLeaveRequest> findAllByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query(value = """
        SELECT lr.* FROM USER_LEAVE_REQUEST lr
        LEFT JOIN USERS u ON lr.user_id = u.id
        WHERE (:#{#sp.userId} IS NULL OR lr.user_id = :#{#sp.userId})
        AND (:#{#sp.status?.name()} IS NULL OR lr.status = :#{#sp.status?.name()})
        AND (:#{#sp.userName} IS NULL OR u.name LIKE CONCAT('%', :#{#sp.userName}, '%'))
        AND (:#{#sp.leaveType?.name()} IS NULL OR lr.leave_type = :#{#sp.leaveType?.name()})
        AND (:#{#sp.periodType?.name()} IS NULL OR lr.period_type = :#{#sp.periodType?.name()})
        AND (:#{#sp.requestDateStart} IS NULL OR lr.request_date >= :#{#sp.requestDateStart})
        AND (:#{#sp.requestDateEnd} IS NULL OR lr.request_date <= :#{#sp.requestDateEnd})
        AND (:#{#sp.createdAtStart} IS NULL OR DATE(lr.created_at) >= :#{#sp.createdAtStart})
        AND (:#{#sp.createdAtEnd} IS NULL OR DATE(lr.created_at) <= :#{#sp.createdAtEnd})
        ORDER BY lr.created_at DESC
        """, nativeQuery = true)
    List<UserLeaveRequest> searchLeaveRequests(@Param("sp") UserLeaveRequestSP sp);

    /**
     * 특정 날짜와 상태로 휴가 요청 조회
     *
     * @param requestDate 요청 날짜
     * @param status 요청 상태
     * @return 휴가 요청 목록
     */
    @Query("""
        SELECT lr FROM UserLeaveRequest lr
        JOIN FETCH lr.user
        WHERE lr.requestDate = :requestDate
        AND lr.status = :status
    """)
    List<UserLeaveRequest> findByRequestDateAndStatus(
        @Param("requestDate") LocalDate requestDate,
        @Param("status") UserLeaveRequest.RequestStatus status
    );

    /**
     * 상태별 휴가 요청 개수 조회 (대시보드용)
     *
     * <p>관리자 대시보드에서 승인 대기 건수를 조회할 때 사용</p>
     *
     * @param status 요청 상태
     * @return 요청 개수
     */
    Long countByStatus(UserLeaveRequest.RequestStatus status);
}
