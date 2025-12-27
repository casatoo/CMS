package io.github.mskim.comm.cms.controller.api;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import io.github.mskim.comm.cms.dto.CacheStatsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 캐시 관리 API 컨트롤러
 *
 * <p>캐시 통계 조회 및 수동 초기화 기능을 제공합니다.</p>
 * <p>관리자 전용 API입니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/cache")
@RequiredArgsConstructor
public class CacheManagementController {

    private final CacheManager cacheManager;

    /**
     * 모든 캐시의 통계 조회
     *
     * <p>Caffeine 캐시의 히트율, 미스율 등의 통계 정보를 반환합니다.</p>
     *
     * @return 캐시별 통계 정보 목록
     */
    @GetMapping("/stats")
    public ResponseEntity<List<CacheStatsDTO>> getCacheStats() {
        List<CacheStatsDTO> statsList = cacheManager.getCacheNames().stream()
            .map(cacheName -> {
                Cache cache = cacheManager.getCache(cacheName);
                if (cache instanceof CaffeineCache caffeineCache) {
                    CacheStats stats = caffeineCache.getNativeCache().stats();

                    return CacheStatsDTO.builder()
                        .cacheName(cacheName)
                        .requestCount(stats.requestCount())
                        .hitCount(stats.hitCount())
                        .missCount(stats.missCount())
                        .hitRate(stats.hitRate())
                        .missRate(stats.missRate())
                        .loadSuccessCount(stats.loadSuccessCount())
                        .loadFailureCount(stats.loadFailureCount())
                        .averageLoadPenalty(stats.averageLoadPenalty())
                        .evictionCount(stats.evictionCount())
                        .evictionWeight(stats.evictionWeight())
                        .build();
                }
                return null;
            })
            .filter(stats -> stats != null)
            .collect(Collectors.toList());

        log.info("캐시 통계 조회 완료: {} 개의 캐시", statsList.size());
        return ResponseEntity.ok(statsList);
    }

    /**
     * 특정 캐시의 상세 통계 조회
     *
     * @param cacheName 캐시 이름
     * @return 캐시 통계 정보
     */
    @GetMapping("/stats/{cacheName}")
    public ResponseEntity<CacheStatsDTO> getCacheStatsByName(@PathVariable String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);

        if (cache == null) {
            log.warn("존재하지 않는 캐시: {}", cacheName);
            return ResponseEntity.notFound().build();
        }

        if (cache instanceof CaffeineCache caffeineCache) {
            CacheStats stats = caffeineCache.getNativeCache().stats();

            CacheStatsDTO statsDTO = CacheStatsDTO.builder()
                .cacheName(cacheName)
                .requestCount(stats.requestCount())
                .hitCount(stats.hitCount())
                .missCount(stats.missCount())
                .hitRate(stats.hitRate())
                .missRate(stats.missRate())
                .loadSuccessCount(stats.loadSuccessCount())
                .loadFailureCount(stats.loadFailureCount())
                .averageLoadPenalty(stats.averageLoadPenalty())
                .evictionCount(stats.evictionCount())
                .evictionWeight(stats.evictionWeight())
                .build();

            log.info("캐시 통계 조회: {} - 히트율 {}", cacheName, stats.hitRate());
            return ResponseEntity.ok(statsDTO);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * 특정 캐시 초기화
     *
     * <p>지정된 캐시의 모든 데이터를 삭제합니다.</p>
     *
     * @param cacheName 초기화할 캐시 이름
     * @return 성공 메시지
     */
    @DeleteMapping("/{cacheName}")
    public ResponseEntity<Map<String, String>> clearCache(@PathVariable String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);

        if (cache == null) {
            log.warn("존재하지 않는 캐시 초기화 시도: {}", cacheName);
            return ResponseEntity.notFound().build();
        }

        cache.clear();
        log.info("캐시 초기화 완료: {}", cacheName);

        Map<String, String> response = new HashMap<>();
        response.put("message", "캐시가 성공적으로 초기화되었습니다");
        response.put("cacheName", cacheName);

        return ResponseEntity.ok(response);
    }

    /**
     * 모든 캐시 초기화
     *
     * <p>시스템의 모든 캐시를 일괄 초기화합니다.</p>
     *
     * @return 성공 메시지
     */
    @DeleteMapping("/all")
    public ResponseEntity<Map<String, Object>> clearAllCaches() {
        List<String> clearedCaches = cacheManager.getCacheNames().stream()
            .peek(cacheName -> {
                Cache cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    cache.clear();
                }
            })
            .collect(Collectors.toList());

        log.info("모든 캐시 초기화 완료: {}", clearedCaches);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "모든 캐시가 성공적으로 초기화되었습니다");
        response.put("clearedCaches", clearedCaches);
        response.put("count", clearedCaches.size());

        return ResponseEntity.ok(response);
    }

    /**
     * 캐시 목록 조회
     *
     * <p>등록된 모든 캐시의 이름을 반환합니다.</p>
     *
     * @return 캐시 이름 목록
     */
    @GetMapping("/names")
    public ResponseEntity<List<String>> getCacheNames() {
        List<String> cacheNames = cacheManager.getCacheNames().stream()
            .collect(Collectors.toList());

        log.info("캐시 목록 조회: {} 개", cacheNames.size());
        return ResponseEntity.ok(cacheNames);
    }
}
