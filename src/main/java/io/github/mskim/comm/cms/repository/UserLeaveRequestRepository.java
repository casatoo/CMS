package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.UserLeaveRequest;
import io.github.mskim.comm.cms.sp.UserLeaveRequestSP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserLeaveRequestRepository extends JpaRepository<UserLeaveRequest, String> {

    @Query("SELECT lr FROM UserLeaveRequest lr WHERE lr.user.id = :userId AND lr.requestDate = :requestDate AND lr.status = 'REQUEST'")
    Optional<UserLeaveRequest> findPendingRequestByUserIdAndDate(@Param("userId") String userId, @Param("requestDate") LocalDate requestDate);

    @Query("SELECT lr FROM UserLeaveRequest lr WHERE lr.requestDate BETWEEN :startDate AND :endDate ORDER BY lr.requestDate DESC")
    List<UserLeaveRequest> findAllByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT lr FROM UserLeaveRequest lr " +
           "WHERE (:#{#sp.status} IS NULL OR lr.status = :#{#sp.status}) " +
           "AND (:#{#sp.userName} IS NULL OR lr.user.name LIKE %:#{#sp.userName}%) " +
           "AND (:#{#sp.leaveType} IS NULL OR lr.leaveType = :#{#sp.leaveType}) " +
           "AND (:#{#sp.periodType} IS NULL OR lr.periodType = :#{#sp.periodType}) " +
           "AND (:#{#sp.requestDateStart} IS NULL OR lr.requestDate >= :#{#sp.requestDateStart}) " +
           "AND (:#{#sp.requestDateEnd} IS NULL OR lr.requestDate <= :#{#sp.requestDateEnd}) " +
           "AND (:#{#sp.createdAtStart} IS NULL OR DATE(lr.createdAt) >= :#{#sp.createdAtStart}) " +
           "AND (:#{#sp.createdAtEnd} IS NULL OR DATE(lr.createdAt) <= :#{#sp.createdAtEnd}) " +
           "ORDER BY lr.createdAt DESC")
    List<UserLeaveRequest> searchLeaveRequests(@Param("sp") UserLeaveRequestSP sp);
}
