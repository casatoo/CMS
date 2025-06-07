package io.github.mskim.comm.cms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @EqualsAndHashCode(callSuper = true)
public class UserAttendanceDTO extends BaseDTO{

    private UserDTO user;                   // 신청자
    private LocalDate workDate;             // 근무일자
    private LocalDateTime checkInTime;      // 출근시간
    private LocalDateTime checkOutTime;     // 퇴근시간
    private String color;                   // 색상 *(캘린더에 표시될 색상 설정을 위해 추가)
}
