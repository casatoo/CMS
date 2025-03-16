package io.github.mskim.comm.cms.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserAttendanceDTO extends BaseDTO{

    private UserDTO user;                   // 신청자
    private LocalDate workDate;             // 근무일자
    private LocalDateTime checkInTime;      // 출근시간
    private LocalDateTime checkOutTime;     // 퇴근시간

}
