package io.github.mskim.comm.cms.exception;

import io.github.mskim.comm.cms.api.ApiResponse;
import io.github.mskim.comm.cms.api.ApiStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("Validation error: {}", errors);

        String errorMessage = errors.values().iterator().next(); // 첫 번째 에러 메시지 사용
        return ResponseEntity.badRequest()
                .body(ApiResponse.of(ApiStatus.BAD_REQUEST, errorMessage, errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        }

        log.warn("Constraint violation: {}", errors);

        String errorMessage = errors.values().iterator().next(); // 첫 번째 에러 메시지 사용
        return ResponseEntity.badRequest()
                .body(ApiResponse.of(ApiStatus.BAD_REQUEST, errorMessage, errors));
    }

    /**
     * 정적 리소스를 찾을 수 없는 경우 처리
     * Chrome DevTools 등 브라우저가 자동으로 요청하는 리소스는 WARN 레벨로 로깅
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        String resourcePath = ex.getResourcePath();

        // Chrome DevTools, favicon 등 브라우저 자동 요청은 WARN 레벨로만 로깅
        if (resourcePath.contains(".well-known") ||
            resourcePath.contains("favicon") ||
            resourcePath.contains("robots.txt")) {
            log.warn("Static resource not found (browser auto-request): {}", resourcePath);
        } else {
            log.error("Static resource not found: {}", resourcePath);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.of(ApiStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."));
    }

    /**
     * BusinessException 처리
     *
     * <p>비즈니스 로직에서 발생한 예외를 처리합니다.</p>
     * <p>ErrorCode에 정의된 HTTP 상태 코드와 메시지를 반환합니다.</p>
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.warn("Business exception occurred: {} - {}", errorCode.getCode(), ex.getMessage());

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.of(
                        mapHttpStatusToApiStatus(errorCode.getHttpStatus()),
                        ex.getMessage()
                ));
    }

    /**
     * IllegalArgumentException 처리
     *
     * <p>잘못된 인자로 인한 예외를 처리합니다.</p>
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.of(ApiStatus.BAD_REQUEST, ex.getMessage()));
    }

    /**
     * IllegalStateException 처리
     *
     * <p>잘못된 상태로 인한 예외를 처리합니다.</p>
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse> handleIllegalStateException(IllegalStateException ex) {
        log.warn("Illegal state: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.of(ApiStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.of(ApiStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."));
    }

    /**
     * HttpStatus를 ApiStatus로 매핑
     *
     * @param httpStatus HTTP 상태 코드
     * @return ApiStatus
     */
    private ApiStatus mapHttpStatusToApiStatus(HttpStatus httpStatus) {
        return switch (httpStatus) {
            case OK -> ApiStatus.OK;
            case CREATED -> ApiStatus.CREATED;
            case ACCEPTED -> ApiStatus.ACCEPTED;
            case BAD_REQUEST -> ApiStatus.BAD_REQUEST;
            case UNAUTHORIZED -> ApiStatus.UNAUTHORIZED;
            case FORBIDDEN -> ApiStatus.FORBIDDEN;
            case NOT_FOUND -> ApiStatus.NOT_FOUND;
            case CONFLICT -> ApiStatus.CONFLICT;
            case INTERNAL_SERVER_ERROR -> ApiStatus.INTERNAL_SERVER_ERROR;
            case SERVICE_UNAVAILABLE -> ApiStatus.SERVICE_UNAVAILABLE;
            case GATEWAY_TIMEOUT -> ApiStatus.GATEWAY_TIMEOUT;
            default -> ApiStatus.INTERNAL_SERVER_ERROR;
        };
    }
}