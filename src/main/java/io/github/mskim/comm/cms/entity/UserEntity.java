package io.github.mskim.comm.cms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users") // 테이블 이름 확인
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    // 기본 생성자
    public UserEntity() {}

}
