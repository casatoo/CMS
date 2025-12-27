package io.github.mskim.comm.cms.sp;

import io.github.mskim.comm.cms.entity.UserLeaveRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserLeaveRequestSP extends BaseSP {

    // 사용자 ID (현재 로그인한 사용자 필터링용)
    private String userId;

    // 신청상태
    private UserLeaveRequest.RequestStatus status;

    // 신청자명
    private String userName;

    // 신청유형
    private UserLeaveRequest.LeaveType leaveType;

    // 기간유형
    private UserLeaveRequest.PeriodType periodType;

    // 신청날짜 범위
    private LocalDate requestDateStart;
    private LocalDate requestDateEnd;

    // 생성일시 범위
    private LocalDate createdAtStart;
    private LocalDate createdAtEnd;
}
