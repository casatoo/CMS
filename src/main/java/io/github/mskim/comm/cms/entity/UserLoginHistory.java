package io.github.mskim.comm.cms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity @Builder @Data @EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@Table(name = "USER_LOGIN_HISTORY", indexes = {
    @Index(name = "idx_user_login_time", columnList = "user_id, login_time"),
    @Index(name = "idx_login_time", columnList = "login_time")
})
public class UserLoginHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Builder.Default
    @Column(name = "login_time", nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime loginTime = LocalDateTime.now();

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Lob
    @Column(name = "user_agent")
    private String userAgent;
}
