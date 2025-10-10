package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.UserAttendanceChangeRequest;
import io.github.mskim.comm.cms.sp.UserAttendanceChangeRequestSP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAttendanceChangeRequestRepository extends JpaRepository<UserAttendanceChangeRequest, String> {

    @Query("SELECT acr FROM UserAttendanceChangeRequest acr " +
           "WHERE (:#{#sp.status} IS NULL OR acr.status = :#{#sp.status}) " +
           "AND (:#{#sp.userName} IS NULL OR acr.user.name LIKE %:#{#sp.userName}%) " +
           "AND (:#{#sp.workDateStart} IS NULL OR acr.workDate >= :#{#sp.workDateStart}) " +
           "AND (:#{#sp.workDateEnd} IS NULL OR acr.workDate <= :#{#sp.workDateEnd}) " +
           "AND (:#{#sp.createdAtStart} IS NULL OR DATE(acr.createdAt) >= :#{#sp.createdAtStart}) " +
           "AND (:#{#sp.createdAtEnd} IS NULL OR DATE(acr.createdAt) <= :#{#sp.createdAtEnd}) " +
           "ORDER BY acr.createdAt DESC")
    List<UserAttendanceChangeRequest> searchAttendanceChangeRequests(@Param("sp") UserAttendanceChangeRequestSP sp);
}
