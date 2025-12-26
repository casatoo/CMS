package io.github.mskim.comm.cms.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 캐시 설정
 *
 * <p>Caffeine 캐시를 사용하여 애플리케이션 성능을 향상시킵니다.</p>
 *
 * <p>캐시 종류:</p>
 * <ul>
 *   <li><b>users</b>: 사용자 정보 (최대 100개, 30분 유지)</li>
 *   <li><b>attendanceSummary</b>: 근태 요약 정보 (최대 500개, 10분 유지)</li>
 *   <li><b>leaveDays</b>: 휴가일수 정보 (최대 200개, 15분 유지)</li>
 * </ul>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 기본 캐시 매니저
     *
     * <p>모든 캐시의 기본 설정을 제공합니다.</p>
     *
     * @return CacheManager
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
            "users",
            "attendanceSummary",
            "leaveDays"
        );

        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .recordStats());

        return cacheManager;
    }

    /**
     * 사용자 정보 전용 캐시 매니저
     *
     * <p>사용자 정보는 자주 조회되지만 크기가 작으므로 별도 관리합니다.</p>
     * <p>접근 시간 기준으로 만료되어 자주 사용되는 사용자는 계속 캐시됩니다.</p>
     *
     * @return CacheManager
     */
    @Bean
    public CacheManager userCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("users");
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .recordStats());

        return cacheManager;
    }
}
