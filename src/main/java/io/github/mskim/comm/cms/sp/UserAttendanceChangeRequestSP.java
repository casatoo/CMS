package io.github.mskim.comm.cms.sp;

import io.github.mskim.comm.cms.entity.UserAttendanceChangeRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data @EqualsAndHashCode(callSuper = true)
public class UserAttendanceChangeRequestSP extends BaseSP{
    private String userId;
    private LocalDate workDate;
    private UserAttendanceChangeRequest.ChangeStatus status;
}
