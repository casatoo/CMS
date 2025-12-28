package io.github.mskim.comm.cms.service;

import io.github.mskim.comm.cms.dto.DashboardStatsDTO;

/**
 * 관리자 대시보드 서비스
 *
 * <p>관리자 대시보드에 표시할 통계 데이터를 제공합니다.</p>
 *
 * @author CMS Team
 * @since 1.0.0
 */
public interface DashboardService {

    /**
     * 대시보드 전체 통계 조회
     *
     * <p>승인 대기 현황, 출근 현황 등 대시보드에 필요한 모든 통계를 조회합니다.</p>
     * <p>결과는 30분간 캐시됩니다.</p>
     *
     * @return 대시보드 통계 데이터
     */
    DashboardStatsDTO getDashboardStats();
}
