package io.github.mskim.comm.cms.serviceImpl;

import io.github.mskim.comm.cms.dto.DashboardStatsDTO;
import io.github.mskim.comm.cms.entity.UserAttendanceChangeRequest;
import io.github.mskim.comm.cms.entity.UserLeaveRequest;
import io.github.mskim.comm.cms.repository.UserAttendanceChangeRequestRepository;
import io.github.mskim.comm.cms.repository.UserAttendanceRepository;
import io.github.mskim.comm.cms.repository.UserLeaveRequestRepository;
import io.github.mskim.comm.cms.repository.UserRepository;
import io.github.mskim.comm.cms.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 관리자 대시보드 서비스 구현
 *
 * <p>대시보드 통계 데이터를 조회하고 캐싱합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final UserAttendanceRepository userAttendanceRepository;
    private final UserAttendanceChangeRequestRepository attendanceChangeRequestRepository;
    private final UserLeaveRequestRepository leaveRequestRepository;

    /**
     * 대시보드 전체 통계 조회
     *
     * <p>승인 대기 건수, 출근 현황, 출근율 등을 계산합니다.</p>
     * <p>캐시 키는 날짜를 포함하여 하루 단위로 구분됩니다.</p>
     *
     * @return DashboardStatsDTO
     */
    @Override
    @Cacheable(value = "dashboardStats")
    public DashboardStatsDTO getDashboardStats() {
        LocalDate today = LocalDate.now();

        // 1. 승인 대기 건수 조회
        Long pendingAttendanceRequests = attendanceChangeRequestRepository
            .countByStatus(UserAttendanceChangeRequest.ChangeStatus.REQUEST);
        Long pendingLeaveRequests = leaveRequestRepository
            .countByStatus(UserLeaveRequest.RequestStatus.REQUEST);

        // 2. 전체 사용자 수 조회
        Long totalUsers = userRepository.count();

        // 3. 오늘 출근 인원 조회
        Long todayCheckInCount = userAttendanceRepository.countByWorkDate(today);

        // 4. 미출근 인원 계산
        Long notCheckedIn = totalUsers - todayCheckInCount;

        // 5. 출근율 계산 (소수점 2자리까지)
        Double attendanceRate = totalUsers > 0
            ? Math.round((todayCheckInCount.doubleValue() / totalUsers.doubleValue() * 100.0) * 100.0) / 100.0
            : 0.0;

        return DashboardStatsDTO.builder()
            .pendingAttendanceChangeRequests(pendingAttendanceRequests)
            .pendingLeaveRequests(pendingLeaveRequests)
            .totalPendingRequests(pendingAttendanceRequests + pendingLeaveRequests)
            .totalUsers(totalUsers)
            .todayCheckInCount(todayCheckInCount)
            .notCheckedInCount(notCheckedIn)
            .attendanceRate(attendanceRate)
            .calculatedAt(LocalDateTime.now())
            .build();
    }
}
