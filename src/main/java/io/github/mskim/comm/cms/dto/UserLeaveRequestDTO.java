package io.github.mskim.comm.cms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @EqualsAndHashCode(callSuper = true)
public class UserLeaveRequestDTO extends BaseDTO{

    private UserDTO user;                       // 신청자
    private LocalDate requestDate;              // 신청일
    private LocalDateTime leaveStartDateTime;   // 휴가 시작일 (DATETIME)
    private LocalDateTime leaveEndDateTime;     // 휴가 종료일 (DATETIME)
    private int requestedHours;                 // 신청한 총 시간
    private String status;                      // REQUEST, APPROVE, REJECT
    private String reason;                      // 사유
    private UserDTO approver;                   // 승인자
    private LocalDateTime approvedAt;           // 승인일
}
