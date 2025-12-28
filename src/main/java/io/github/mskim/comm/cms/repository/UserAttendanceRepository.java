package io.github.mskim.comm.cms.repository;

import io.github.mskim.comm.cms.entity.UserAttendance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 사용자 근태 Repository
 *
 * <p>MyBatis에서 JPA로 마이그레이션 완료</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
public interface UserAttendanceRepository extends BaseRepository<UserAttendance, String> {

    // ========================================
    // Query Method 방식 (간단한 쿼리)
    // ========================================

    /**
     * 특정 기간의 근태 기록 조회
     *
     * @param userId 사용자 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 기간 내 근태 기록 목록
     */
    List<UserAttendance> findByUserIdAndWorkDateBetween(
        String userId,
        LocalDate startDate,
        LocalDate endDate
    );

    /**
     * 특정 사용자의 특정 날짜 근태 기록 조회
     *
     * @param userId 사용자 ID
     * @param workDate 근무일
     * @return 근태 기록 (Optional)
     */
    Optional<UserAttendance> findByUserIdAndWorkDate(String userId, LocalDate workDate);

    /**
     * 특정 사용자의 특정 날짜에 근태 기록 존재 여부 확인
     *
     * @param userId 사용자 ID
     * @param workDate 근무일
     * @return 존재 여부
     */
    boolean existsByUserIdAndWorkDate(String userId, LocalDate workDate);

    // ========================================
    // @Query 방식 (복잡한 쿼리)
    // ========================================

    /**
     * 오늘 날짜의 출근 시간 조회 (MyBatis: findTodayCheckInTime)
     *
     * <p>User 정보를 함께 Fetch Join으로 조회</p>
     *
     * @param userId 사용자 ID
     * @param today 오늘 날짜
     * @return 오늘의 근태 기록 (Optional)
     */
    @Query("""
        SELECT ua FROM UserAttendance ua
        JOIN FETCH ua.user u
        WHERE ua.user.id = :userId
        AND ua.workDate = :today
    """)
    Optional<UserAttendance> findTodayCheckInTime(
        @Param("userId") String userId,
        @Param("today") LocalDate today
    );

    /**
     * 이번 달 근무일수 조회 (MyBatis: countWorkDaysThisMonth)
     *
     * @param userId 사용자 ID
     * @param startOfMonth 이번 달 1일
     * @param endOfMonth 이번 달 마지막 날
     * @return 근무일수
     */
    @Query("""
        SELECT COUNT(ua) FROM UserAttendance ua
        WHERE ua.user.id = :userId
        AND ua.workDate BETWEEN :startOfMonth AND :endOfMonth
    """)
    Long countWorkDaysThisMonth(
        @Param("userId") String userId,
        @Param("startOfMonth") LocalDate startOfMonth,
        @Param("endOfMonth") LocalDate endOfMonth
    );

    /**
     * 특정 날짜의 근태 기록 조회 (MyBatis: findAttendanceByDate)
     *
     * <p>User 정보를 함께 Fetch Join으로 조회</p>
     *
     * @param userId 사용자 ID
     * @param workDate 근무일
     * @return 근태 기록 (Optional)
     */
    @Query("""
        SELECT ua FROM UserAttendance ua
        JOIN FETCH ua.user u
        WHERE ua.user.id = :userId
        AND ua.workDate = :workDate
    """)
    Optional<UserAttendance> findAttendanceByDate(
        @Param("userId") String userId,
        @Param("workDate") LocalDate workDate
    );

    /**
     * 특정 기간의 근태 기록 조회 (MyBatis: findAttendanceInRange)
     *
     * <p>User 정보를 함께 Fetch Join으로 조회하여 N+1 문제 방지</p>
     *
     * @param userId 사용자 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 기간 내 근태 기록 목록
     */
    @Query("""
        SELECT ua FROM UserAttendance ua
        JOIN FETCH ua.user u
        WHERE ua.user.id = :userId
        AND ua.workDate BETWEEN :startDate AND :endDate
        ORDER BY ua.workDate ASC
    """)
    List<UserAttendance> findAttendanceInRange(
        @Param("userId") String userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    /**
     * 기간 내 모든 사용자의 근태 기록 조회 (User 정보 포함)
     *
     * <p>관리자 페이지에서 전체 근태 현황 조회 시 사용</p>
     *
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 기간 내 모든 근태 기록 목록
     */
    @Query("""
        SELECT ua FROM UserAttendance ua
        JOIN FETCH ua.user u
        WHERE ua.workDate BETWEEN :startDate AND :endDate
        ORDER BY ua.workDate DESC, u.name ASC
    """)
    List<UserAttendance> findAllAttendanceInRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    /**
     * 평균 출근 시간 계산 (Native Query 사용)
     *
     * <p>JPQL에서는 HOUR() 함수가 제대로 지원되지 않아 Native Query 사용</p>
     *
     * @param userId 사용자 ID
     * @param startOfMonth 이번 달 1일
     * @param endOfMonth 이번 달 마지막 날
     * @return 평균 출근 시간 (시)
     */
    @Query(value = """
        SELECT AVG(HOUR(check_in_time))
        FROM USER_ATTENDANCE
        WHERE user_id = :userId
        AND work_date BETWEEN :startOfMonth AND :endOfMonth
        AND check_in_time IS NOT NULL
    """, nativeQuery = true)
    Double averageCheckInHour(
        @Param("userId") String userId,
        @Param("startOfMonth") LocalDate startOfMonth,
        @Param("endOfMonth") LocalDate endOfMonth
    );

    /**
     * 사용자명으로 근태 기록 조회 (최신순)
     *
     * <p>출퇴근 기록 화면에서 사용자명만으로 조회 시 사용</p>
     *
     * @param userName 사용자명 (부분 일치)
     * @return 근태 기록 목록 (최신순 정렬)
     */
    @Query("""
        SELECT ua FROM UserAttendance ua
        JOIN FETCH ua.user u
        WHERE u.name LIKE %:userName%
        ORDER BY ua.workDate DESC
    """)
    List<UserAttendance> findByUserNameContaining(@Param("userName") String userName);
}
