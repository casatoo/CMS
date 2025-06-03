package io.github.mskim.comm.cms.dto;

import io.github.mskim.comm.cms.entity.UserAttendance;
import io.github.mskim.comm.cms.entity.Users;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAttendanceChangeRequestDTO extends BaseDTO {

    private UserAttendance attendance;              // 대상 출퇴근 기록
    private Users user;                             // 변경 신청자
    private LocalDateTime originalCheckInTime;      // 기존 출근 시간
    private LocalDateTime originalCheckOutTime;     // 기존 퇴근 시간
    private LocalDateTime requestedCheckInTime;     // 변경 요청 출근 시간
    private LocalDateTime requestedCheckOutTime;    // 변경 요청 퇴근 시간
    private String reason;                          // 변경 사유
    private String status;                          // 기본값 REQUEST
    private Users approver;                         // 승인자
    private LocalDateTime approvedAt;               // 승인 일시

}
