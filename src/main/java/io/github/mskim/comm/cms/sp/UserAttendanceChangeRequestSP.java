package io.github.mskim.comm.cms.sp;

import io.github.mskim.comm.cms.entity.UserAttendanceChangeRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserAttendanceChangeRequestSP extends BaseSP {

    // 사용자 ID (MyBatis Mapper에서 사용)
    private String userId;

    // 근무일 (MyBatis Mapper에서 사용)
    private LocalDate workDate;

    // 신청상태
    private UserAttendanceChangeRequest.ChangeStatus status;

    // 신청자명 (검색용)
    private String userName;

    // 근무일 범위 (검색용)
    private LocalDate workDateStart;
    private LocalDate workDateEnd;

    // 생성일시 범위 (검색용)
    private LocalDate createdAtStart;
    private LocalDate createdAtEnd;
}
