package io.github.mskim.comm.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 출퇴근 기록 조회용 DTO
 *
 * <p>전체 사용자의 출퇴근 시간과 휴가 상태를 포함합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceHistoryDTO {

    private String userId;              // 사용자 ID
    private String userName;            // 사용자명
    private LocalDate workDate;         // 근무일자
    private LocalDateTime checkInTime;  // 출근시간
    private LocalDateTime checkOutTime; // 퇴근시간
    private Double workHours;           // 근무시간
    private String leaveStatus;         // 휴가 상태 (연차/외근/출장)
}
