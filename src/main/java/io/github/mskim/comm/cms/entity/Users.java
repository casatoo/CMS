package io.github.mskim.comm.cms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity @Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "USERS")
public class Users extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String rank;

    public Users() {}

    public void setId(String id) {
        // Do nothing or throw exception
    }
}
