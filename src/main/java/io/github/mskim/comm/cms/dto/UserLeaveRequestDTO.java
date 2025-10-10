package io.github.mskim.comm.cms.dto;

import io.github.mskim.comm.cms.entity.UserLeaveRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data @EqualsAndHashCode(callSuper = true)
public class UserLeaveRequestDTO extends BaseDTO {

    @NotNull(message = "신청 유형은 필수입니다")
    private UserLeaveRequest.LeaveType leaveType; // 신청 유형

    @NotNull(message = "신청 날짜는 필수입니다")
    private LocalDate requestDate; // 신청 날짜

    @NotNull(message = "기간 유형은 필수입니다")
    private UserLeaveRequest.PeriodType periodType; // 기간 유형

    @Size(max = 200, message = "장소는 200자 이하로 입력해주세요")
    private String location; // 장소 (출장/외근의 경우)

    @NotBlank(message = "신청 사유는 필수입니다")
    @Size(min = 10, max = 500, message = "신청 사유는 10자 이상 500자 이하로 입력해주세요")
    private String reason; // 신청 사유

    private UserLeaveRequest.RequestStatus status; // 상태
}
