package io.github.mskim.comm.cms.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Resilience4JSample {

    @CircuitBreaker(name = "MainController_Method1", fallbackMethod = "fallBackMethod") // 이 서킷 브레이커의 명칭, 실패 시 실행할 메서드명
    @GetMapping("/method1")
    public String method1() {
        simulateDelayOrError(); // 지연 테스트
        return "Method1 Response";
    }

    @Retry(name="MainController_Method2",fallbackMethod = "fallBackMethod") // retry 명칭 , 실패 시 실행할 메서드명
    @GetMapping("/method2")
    public String method2() {
        simulateError(); // 오류 발생 테스트
        return "Method2 Response";
    }

    @Bulkhead(name = "MainController_Method3", type = Bulkhead.Type.SEMAPHORE, fallbackMethod = "fallBackMethod") // semaphore 방식 ( 요청갯수 제한 )
    @GetMapping("/method3")
    public String method3() {
        return "Method3 Response";
    }

    @Bulkhead(name = "MainController_Method4", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "fallBackMethod") // threadpool 방식 ( 스레드 제한 )
    @GetMapping("/method4")
    public String method4() {
        return "Method4 Response";
    }

    // 서킷 브레이커/재시도 실패 시 호출될 Fallback 메서드
    public String fallbackMethod(Exception e) {
        return "Fallback Response: " + e.getMessage();
    }

    // 지연 테스트를 위한 메서드
    private void simulateDelayOrError() {
        if (Math.random() < 0.5) { // 50% 확률로 에러 발생
            throw new RuntimeException("Simulated Error");
        }
        try {
            Thread.sleep(4000); // 4초 대기 (서킷 브레이커 설정된 3초를 초과)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void simulateError() {
        throw new RuntimeException("Simulated Failure");
    }

}
