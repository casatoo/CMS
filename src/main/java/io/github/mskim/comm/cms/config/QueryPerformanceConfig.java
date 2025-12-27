package io.github.mskim.comm.cms.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 쿼리 성능 모니터링 설정
 *
 * <p>Repository 메서드 실행 시간을 측정하고 슬로우 쿼리를 감지합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
public class QueryPerformanceConfig {

    /**
     * 슬로우 쿼리 기준 시간 (밀리초)
     */
    private static final long SLOW_QUERY_THRESHOLD_MS = 1000;

    /**
     * Repository 메서드 실행 시간 측정
     *
     * <p>실행 시간이 1초 이상일 경우 슬로우 쿼리로 로깅합니다.</p>
     *
     * @param joinPoint AOP 조인포인트
     * @return 메서드 실행 결과
     * @throws Throwable 메서드 실행 중 발생한 예외
     */
    @Around("execution(* io.github.mskim.comm.cms.repository..*(..))")
    public Object logQueryPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - startTime;

            if (executionTime > SLOW_QUERY_THRESHOLD_MS) {
                log.warn("⚠️ SLOW QUERY DETECTED: {} - {}ms", methodName, executionTime);
            } else {
                log.debug("Query executed: {} - {}ms", methodName, executionTime);
            }

            return result;
        } catch (Throwable e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("Query failed: {} - {}ms - Error: {}", methodName, executionTime, e.getMessage());
            throw e;
        }
    }
}
