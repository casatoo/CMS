package io.github.mskim.comm.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 캐시 통계 DTO
 *
 * <p>Caffeine 캐시의 통계 정보를 담는 DTO입니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheStatsDTO {

    /**
     * 캐시 이름
     */
    private String cacheName;

    /**
     * 총 요청 수 (히트 + 미스)
     */
    private long requestCount;

    /**
     * 캐시 히트 수
     */
    private long hitCount;

    /**
     * 캐시 미스 수
     */
    private long missCount;

    /**
     * 캐시 히트율 (0.0 ~ 1.0)
     */
    private double hitRate;

    /**
     * 캐시 미스율 (0.0 ~ 1.0)
     */
    private double missRate;

    /**
     * 로드 성공 횟수
     */
    private long loadSuccessCount;

    /**
     * 로드 실패 횟수
     */
    private long loadFailureCount;

    /**
     * 평균 로드 시간 (나노초)
     */
    private double averageLoadPenalty;

    /**
     * 캐시 제거(eviction) 횟수
     */
    private long evictionCount;

    /**
     * 캐시 제거(eviction) 바이트 수
     */
    private long evictionWeight;
}
