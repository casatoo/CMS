package io.github.mskim.comm.cms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Builder
@Table(name = "USER_LEAVE_REQUEST")
@Data @EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
public class UserLeaveRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; // 연차 신청자

    @Builder.Default
    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate = LocalDate.now(); // 신청 날짜

    @Column(name = "leave_start_datetime", nullable = false)
    private LocalDateTime leaveStartDateTime; // 휴가 시작일 (DATETIME)

    @Column(name = "leave_end_datetime", nullable = false)
    private LocalDateTime leaveEndDateTime; // 휴가 종료일 (DATETIME)

    @Column(name = "requested_hours", nullable = false)
    private int requestedHours; // 신청한 총 시간

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private LeaveStatus status = LeaveStatus.REQUEST; // 기본값 REQUEST

    @Column(name = "reason")
    private String reason; // 휴가 사유

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = true)
    private Users approver; // 승인자

    @Column(name = "approved_at")
    private LocalDateTime approvedAt; // 승인 시간

    public enum LeaveStatus {
        REQUEST, APPROVE, REJECT
    }
}
