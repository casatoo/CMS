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
    @ToString.Exclude
    private Users user; // 신청자

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType; // 신청 유형

    @Column(name = "request_date", nullable = false)
    private LocalDate requestDate; // 신청 날짜

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false)
    private PeriodType periodType; // 기간 유형

    @Column(name = "location", length = 200)
    private String location; // 장소 (출장/외근의 경우)

    @Column(name = "reason", columnDefinition = "TEXT", nullable = false)
    private String reason; // 신청 사유

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status = RequestStatus.REQUEST; // 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    @ToString.Exclude
    private Users approver; // 승인자

    @Column(name = "approved_at")
    private LocalDateTime approvedAt; // 승인 일시

    public enum LeaveType {
        ANNUAL_LEAVE,  // 연차
        BUSINESS_TRIP, // 출장
        FIELD_WORK     // 외근
    }

    public enum PeriodType {
        ALL_DAY,   // 종일
        MORNING,   // 오전
        AFTERNOON  // 오후
    }

    public enum RequestStatus {
        REQUEST,  // 신청
        APPROVE,  // 승인
        REJECT    // 반려
    }
}
