package io.github.mskim.comm.cms.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiStatus {

    OK(200),                        // 요청 성공
    CREATED(201),                   // 리소스 생성
    ACCEPTED(202),                  // 비동기 처리 응답
    BAD_REQUEST(400),               // 잘못된 요청
    UNAUTHORIZED(401),              // 인증 오류
    FORBIDDEN(403),                 // 접근권한 없음
    NOT_FOUND(404),                 // 잘못된 URL
    CONFLICT(409),                  // 중복 데이터 충돌
    INTERNAL_SERVER_ERROR(500),     // 서버 오류
    SERVICE_UNAVAILABLE(503),       // 서버 과부화
    GATEWAY_TIMEOUT(504);           // 응답시간 초과

    private final int code;

}
