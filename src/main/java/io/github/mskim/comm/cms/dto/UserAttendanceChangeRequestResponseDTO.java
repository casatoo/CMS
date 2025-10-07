package io.github.mskim.comm.cms.dto;

import io.github.mskim.comm.cms.entity.UserAttendanceChangeRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAttendanceChangeRequestResponseDTO {

    private String id;
    private String userId;
    private String userName;
    private String userLoginId;
    private String attendanceId;
    private LocalDateTime originalCheckInTime;
    private LocalDateTime originalCheckOutTime;
    private LocalDateTime requestedCheckInTime;
    private LocalDateTime requestedCheckOutTime;
    private String reason;
    private UserAttendanceChangeRequest.ChangeStatus status;
    private String approverId;
    private String approverName;
    private String approverLoginId;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserAttendanceChangeRequestResponseDTO from(UserAttendanceChangeRequest entity) {
        return UserAttendanceChangeRequestResponseDTO.builder()
                .id(entity.getId())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .userName(entity.getUser() != null ? entity.getUser().getName() : null)
                .userLoginId(entity.getUser() != null ? entity.getUser().getLoginId() : null)
                .attendanceId(entity.getAttendance() != null ? entity.getAttendance().getId() : null)
                .originalCheckInTime(entity.getOriginalCheckInTime())
                .originalCheckOutTime(entity.getOriginalCheckOutTime())
                .requestedCheckInTime(entity.getRequestedCheckInTime())
                .requestedCheckOutTime(entity.getRequestedCheckOutTime())
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
