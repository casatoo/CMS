package io.github.mskim.comm.cms.dto;

import io.github.mskim.comm.cms.entity.UserLeaveRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLeaveRequestResponseDTO {

    private String id;
    private String userId;
    private String userName;
    private String userLoginId;
    private UserLeaveRequest.LeaveType leaveType;
    private LocalDate requestDate;
    private UserLeaveRequest.PeriodType periodType;
    private String location;
    private String reason;
    private UserLeaveRequest.RequestStatus status;
    private String approverId;
    private String approverName;
    private String approverLoginId;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserLeaveRequestResponseDTO from(UserLeaveRequest entity) {
        return UserLeaveRequestResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .userName(entity.getUser() != null ? entity.getUser().getName() : null)
                .userLoginId(entity.getUser() != null ? entity.getUser().getLoginId() : null)
                .leaveType(entity.getLeaveType())
                .requestDate(entity.getRequestDate())
                .periodType(entity.getPeriodType())
                .location(entity.getLocation())
                .reason(entity.getReason())
                .status(entity.getStatus())
                .approverId(entity.getApprover() != null ? entity.getApprover().getId() : null)
                .approverName(entity.getApprover() != null ? entity.getApprover().getName() : null)
                .approverLoginId(entity.getApprover() != null ? entity.getApprover().getLoginId() : null)
                .approvedAt(entity.getApprovedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
