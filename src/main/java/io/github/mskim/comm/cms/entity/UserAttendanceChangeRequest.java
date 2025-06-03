package io.github.mskim.comm.cms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Builder @Table(name = "USER_ATTENDANCE_CHANGE_REQUEST")
@Data @EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
public class UserAttendanceChangeRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id", nullable = false)
    private UserAttendance attendance; // 대상 출퇴근 기록

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; // 변경 신청자

    @Column(name = "original_check_in_time", nullable = false)
    private LocalDateTime originalCheckInTime; // 기존 출근 시간

    @Column(name = "original_check_out_time")
    private LocalDateTime originalCheckOutTime; // 기존 퇴근 시간

    @Column(name = "requested_check_in_time", nullable = false)
    private LocalDateTime requestedCheckInTime; // 변경 요청 출근 시간

    @Column(name = "requested_check_out_time")
    private LocalDateTime requestedCheckOutTime; // 변경 요청 퇴근 시간

    @Column(name = "reason")
    private String reason; // 변경 사유

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChangeStatus status = ChangeStatus.REQUEST; // 기본값 REQUEST

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private Users approver; // 승인자

    @Column(name = "approved_at")
    private LocalDateTime approvedAt; // 승인 일시

    public enum ChangeStatus {
        REQUEST, APPROVE, REJECT
    }
}
