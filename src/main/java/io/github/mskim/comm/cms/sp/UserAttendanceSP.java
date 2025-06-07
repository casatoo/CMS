package io.github.mskim.comm.cms.sp;

import io.github.mskim.comm.cms.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data @EqualsAndHashCode(callSuper = true)
public class UserAttendanceSP extends BaseSP {
    private String userId;
    private String month;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate workDate;
}
