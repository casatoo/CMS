package io.github.mskim.comm.cms.dto;

import java.time.LocalDateTime;

public class UserLoginHistoryDTO extends BaseDTO{

    private UserDTO user;               // 사용자
    private LocalDateTime loginTime;    // 로그인 시간
    private String ipAddress;           // 아이피
    private String userAgent;           // 접속정보 (운영체제, 브라우저 ..)
}
