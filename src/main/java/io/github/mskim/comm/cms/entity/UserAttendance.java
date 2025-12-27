package io.github.mskim.comm.cms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Builder @Data @EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@Table(name = "USER_ATTENDANCE",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "work_date"}),
    indexes = {
        @Index(name = "idx_work_date", columnList = "work_date"),
        @Index(name = "idx_created_at", columnList = "created_at")
    }
)
public class UserAttendance extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; // 출근 사용자

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate; // 근무 날짜

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime; // 출근 시간

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime; // 퇴근 시간 (nullable)
}
