package io.github.mskim.comm.cms.dto;

import lombok.Data;

@Data
public class JoinDTO {

    private String loginId;

    private String password;

    private String name;

    private String role;

    private String rank;
}
