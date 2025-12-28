package io.github.mskim.comm.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 관리자 대시보드 통계 DTO
 *
 * <p>대시보드에 표시할 전체 시스템 통계 데이터를 담는 DTO입니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {

    // ========================================
    // 승인 대기 현황
    // ========================================

    /**
     * 근태 수정 요청 대기 건수
     */
    private Long pendingAttendanceChangeRequests;

    /**
     * 휴가 신청 대기 건수
     */
    private Long pendingLeaveRequests;

    /**
     * 전체 승인 대기 건수 (근태 + 휴가)
     */
    private Long totalPendingRequests;

    // ========================================
    // 출근 현황
    // ========================================

    /**
     * 전체 사용자 수
     */
    private Long totalUsers;

    /**
     * 오늘 출근 인원
     */
    private Long todayCheckInCount;

    /**
     * 미출근 인원
     */
    private Long notCheckedInCount;

    /**
     * 출근율 (%)
     */
    private Double attendanceRate;

    // ========================================
    // 메타 정보
    // ========================================

    /**
     * 통계 계산 시간
     */
    private LocalDateTime calculatedAt;
}
