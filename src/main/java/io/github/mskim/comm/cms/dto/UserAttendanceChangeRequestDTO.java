package io.github.mskim.comm.cms.dto;

import io.github.mskim.comm.cms.entity.UserAttendance;
import io.github.mskim.comm.cms.entity.UserAttendanceChangeRequest;
import io.github.mskim.comm.cms.entity.Users;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data @EqualsAndHashCode(callSuper = true)
public class UserAttendanceChangeRequestDTO extends BaseDTO {

    @NotNull(message = "출퇴근 기록은 필수입니다")
    private UserAttendance attendance;                          // 대상 출퇴근 기록

    private Users user;                                         // 변경 신청자
    private LocalDateTime originalCheckInTime;                  // 기존 출근 시간
    private LocalDateTime originalCheckOutTime;                 // 기존 퇴근 시간

    @NotNull(message = "변경 요청 출근 시간은 필수입니다")
    private LocalDateTime requestedCheckInTime;                 // 변경 요청 출근 시간

    private LocalDateTime requestedCheckOutTime;                // 변경 요청 퇴근 시간

    @NotBlank(message = "변경 사유는 필수입니다")
    @Size(min = 10, max = 500, message = "변경 사유는 10자 이상 500자 이하로 입력해주세요")
    private String reason;                                      // 변경 사유

    private UserAttendanceChangeRequest.ChangeStatus status;    // 기본값 REQUEST
    private Users approver;                                     // 승인자
    private LocalDateTime approvedAt;                           // 승인 일시

}
