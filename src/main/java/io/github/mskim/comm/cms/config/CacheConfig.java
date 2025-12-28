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
 *   <li><b>users</b>: 사용자 정보 (최대 1000개, 30분 유지)</li>
 *   <li><b>attendanceSummary</b>: 월별 근무일수 집계 (최대 1000개, 30분 유지)</li>
 *   <li><b>dailyAttendance</b>: 일별 근태 기록 (최대 1000개, 30분 유지)</li>
 *   <li><b>attendanceList</b>: 월별 근태 목록 (최대 1000개, 30분 유지)</li>
 *   <li><b>leaveDays</b>: 휴가일수 정보 (최대 1000개, 30분 유지)</li>
 * </ul>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 캐시 매니저
     *
     * <p>모든 캐시의 설정을 제공합니다.</p>
     * <p>사용자 정보는 접근 시간 기준으로 30분간 유지됩니다.</p>
     *
     * @return CacheManager
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
            "users",
            "attendanceSummary",
            "dailyAttendance",
            "attendanceList",
            "leaveDays",
            "dashboardStats"
        );

        // 사용자 정보는 자주 조회되므로 접근 시간 기준으로 캐시 유지
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .recordStats());

        return cacheManager;
    }
}
