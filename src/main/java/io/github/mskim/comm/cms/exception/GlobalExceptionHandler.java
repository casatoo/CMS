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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.of(ApiStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."));
    }
}