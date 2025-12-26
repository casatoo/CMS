package io.github.mskim.comm.cms.exception;

import lombok.Getter;

/**
 * 비즈니스 예외 기본 클래스
 *
 * <p>비즈니스 로직에서 발생하는 예외를 표현합니다.</p>
 * <p>ErrorCode를 기반으로 예외를 생성하며, 추가 메시지를 제공할 수 있습니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    /**
     * ErrorCode로 예외 생성
     *
     * @param errorCode 에러 코드
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode와 커스텀 메시지로 예외 생성
     *
     * @param errorCode 에러 코드
     * @param message 커스텀 메시지
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode와 원인 예외로 예외 생성
     *
     * @param errorCode 에러 코드
     * @param cause 원인 예외
     */
    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    /**
     * ErrorCode, 커스텀 메시지, 원인 예외로 예외 생성
     *
     * @param errorCode 에러 코드
     * @param message 커스텀 메시지
     * @param cause 원인 예외
     */
    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
