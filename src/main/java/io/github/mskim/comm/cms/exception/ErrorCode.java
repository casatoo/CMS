package io.github.mskim.comm.cms.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 정의
 *
 * <p>애플리케이션에서 발생할 수 있는 모든 에러를 정의합니다.</p>
 * <p>각 에러는 HTTP 상태 코드, 에러 코드, 메시지를 가집니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ========================================
    // 인증/인가 (AUTH)
    // ========================================
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH001", "인증되지 않은 사용자입니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH002", "권한이 없습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH003", "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH004", "만료된 토큰입니다"),

    // ========================================
    // 사용자 (USER)
    // ========================================
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER001", "사용자를 찾을 수 없습니다"),
    DUPLICATE_LOGIN_ID(HttpStatus.CONFLICT, "USER002", "이미 존재하는 로그인 아이디입니다"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "USER003", "잘못된 비밀번호입니다"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER004", "이미 존재하는 사용자입니다"),

    // ========================================
    // 근태 (ATTENDANCE)
    // ========================================
    ALREADY_CHECKED_IN(HttpStatus.CONFLICT, "ATT001", "이미 출근 처리되었습니다"),
    ALREADY_CHECKED_OUT(HttpStatus.CONFLICT, "ATT002", "이미 퇴근 처리되었습니다"),
    NOT_CHECKED_IN(HttpStatus.BAD_REQUEST, "ATT003", "출근 기록이 없습니다"),
    ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "ATT004", "근태 기록을 찾을 수 없습니다"),
    INVALID_ATTENDANCE_DATE(HttpStatus.BAD_REQUEST, "ATT005", "유효하지 않은 근태 날짜입니다"),

    // ========================================
    // 근태 변경 요청 (ATTENDANCE_CHANGE_REQUEST)
    // ========================================
    CHANGE_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "ACR001", "근태 변경 요청을 찾을 수 없습니다"),
    CHANGE_REQUEST_ALREADY_EXISTS(HttpStatus.CONFLICT, "ACR002", "이미 변경 신청 중인 날짜입니다"),
    CHANGE_REQUEST_ALREADY_PROCESSED(HttpStatus.CONFLICT, "ACR003", "이미 처리된 신청입니다"),
    INVALID_CHANGE_REQUEST_STATUS(HttpStatus.BAD_REQUEST, "ACR004", "유효하지 않은 신청 상태입니다"),

    // ========================================
    // 휴가 요청 (LEAVE_REQUEST)
    // ========================================
    LEAVE_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "LVR001", "휴가 요청을 찾을 수 없습니다"),
    LEAVE_REQUEST_ALREADY_EXISTS(HttpStatus.CONFLICT, "LVR002", "이미 신청 중인 날짜입니다"),
    LEAVE_REQUEST_ALREADY_PROCESSED(HttpStatus.CONFLICT, "LVR003", "이미 처리된 신청입니다"),
    INVALID_LEAVE_DATE(HttpStatus.BAD_REQUEST, "LVR004", "유효하지 않은 휴가 날짜입니다"),
    INSUFFICIENT_LEAVE_DAYS(HttpStatus.BAD_REQUEST, "LVR005", "잔여 휴가일수가 부족합니다"),

    // ========================================
    // 공통 (COMMON)
    // ========================================
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COM001", "잘못된 입력 값입니다"),
    MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST, "COM002", "필수 입력 항목이 누락되었습니다"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "COM003", "요청한 리소스를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COM004", "서버 내부 오류가 발생했습니다"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COM005", "데이터베이스 오류가 발생했습니다"),
    EXTERNAL_API_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "COM006", "외부 API 호출 중 오류가 발생했습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
