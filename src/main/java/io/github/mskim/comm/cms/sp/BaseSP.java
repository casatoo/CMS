package io.github.mskim.comm.cms.sp;

import java.time.LocalDateTime;

public abstract class BaseSP {

    private String id; // UUID 문자열
    private LocalDateTime createdAt; // 생성 시간
    private String createdBy; // 생성자
    private LocalDateTime updatedAt; // 수정 시간
    private String updatedBy; // 수정자

}
