package io.github.mskim.comm.cms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Builder
@Table(name = "USER_ATTENDANCE_CHANGE_REQUEST", indexes = {
    @Index(name = "idx_user_status", columnList = "user_id, status"),
    @Index(name = "idx_status_created", columnList = "status, created_at"),
    @Index(name = "idx_work_date", columnList = "work_date"),
    @Index(name = "idx_attendance", columnList = "attendance_id")
})
@Data @EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
public class UserAttendanceChangeRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id")
    @ToString.Exclude
    private UserAttendance attendance; // 대상 출퇴근 기록

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private Users user; // 변경 신청자

    @Column(name = "work_date", nullable = false)
    private java.time.LocalDate workDate; // 근무일 (attendance가 null일 수 있으므로 별도 필드로 관리)

    @Column(name = "original_check_in_time")
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
    private ChangeStatus status = ChangeStatus.REQUEST;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    @ToString.Exclude
    private Users approver; // 승인자

    @Column(name = "approved_at")
    private LocalDateTime approvedAt; // 승인 일시

    public enum ChangeStatus {
        REQUEST, APPROVE, REJECT
    }
}