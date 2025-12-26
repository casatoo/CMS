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

    @Query("SELECT lr FROM UserLeaveRequest lr WHERE lr.user.id = :userId AND lr.requestDate = :requestDate AND lr.status = 'REQUEST'")
    Optional<UserLeaveRequest> findPendingRequestByUserIdAndDate(@Param("userId") String userId, @Param("requestDate") LocalDate requestDate);

    @Query("SELECT lr FROM UserLeaveRequest lr WHERE lr.requestDate BETWEEN :startDate AND :endDate ORDER BY lr.requestDate DESC")
    List<UserLeaveRequest> findAllByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = """
        SELECT lr.* FROM USER_LEAVE_REQUEST lr
        LEFT JOIN USERS u ON lr.user_id = u.id
        WHERE (:#{#sp.status} IS NULL OR lr.status = :#{#sp.status})
        AND (:#{#sp.userName} IS NULL OR u.name LIKE CONCAT('%', :#{#sp.userName}, '%'))
        AND (:#{#sp.leaveType} IS NULL OR lr.leave_type = :#{#sp.leaveType})
        AND (:#{#sp.periodType} IS NULL OR lr.period_type = :#{#sp.periodType})
        AND (:#{#sp.requestDateStart} IS NULL OR lr.request_date >= :#{#sp.requestDateStart})
        AND (:#{#sp.requestDateEnd} IS NULL OR lr.request_date <= :#{#sp.requestDateEnd})
        AND (:#{#sp.createdAtStart} IS NULL OR DATE(lr.created_at) >= :#{#sp.createdAtStart})
        AND (:#{#sp.createdAtEnd} IS NULL OR DATE(lr.created_at) <= :#{#sp.createdAtEnd})
        ORDER BY lr.created_at DESC
        """, nativeQuery = true)
    List<UserLeaveRequest> searchLeaveRequests(@Param("sp") UserLeaveRequestSP sp);
}
