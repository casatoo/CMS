package io.github.mskim.comm.cms.dto;

import lombok.Data;

@Data
public class UserDTO extends BaseDTO{

    private String loginId;         // 로그인 아이디
    private String password;        // 비밀번호
    private String name;            // 이름
    private int annualLeaveDays;    // 연차
    private String role;            // 권한
    private String rank;            // 직급
}
