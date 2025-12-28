package io.github.mskim.comm.cms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * CORS 설정을 관리하는 Properties 클래스
 *
 * <p>환경변수 CORS_ALLOWED_ORIGINS에서 콤마로 구분된 URL 목록을 받아
 * List<String>으로 변환합니다.</p>
 *
 * <p>예시: CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8081</p>
 */
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

    @Setter
    private String allowed;

    /**
     * 허용된 CORS origin 목록 반환
     *
     * @return 콤마로 구분된 origin 문자열을 List로 변환하여 반환
     */
    public List<String> getAllowed() {
        if (allowed == null || allowed.trim().isEmpty()) {
            return Arrays.asList("http://localhost");
        }
        return Arrays.stream(allowed.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toList();
    }
}
